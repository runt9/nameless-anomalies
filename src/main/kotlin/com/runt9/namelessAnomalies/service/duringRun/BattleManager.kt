package com.runt9.namelessAnomalies.service.duringRun

import com.runt9.namelessAnomalies.model.anomaly.Anomaly
import com.runt9.namelessAnomalies.model.anomaly.tdr
import com.runt9.namelessAnomalies.model.event.BattleComplete
import com.runt9.namelessAnomalies.model.event.HpChanged
import com.runt9.namelessAnomalies.model.event.PlayerTurnReady
import com.runt9.namelessAnomalies.model.event.RunEndEvent
import com.runt9.namelessAnomalies.model.event.TurnComplete
import com.runt9.namelessAnomalies.model.interceptor.InterceptableAdapter
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook
import com.runt9.namelessAnomalies.service.RandomizerService
import com.runt9.namelessAnomalies.util.ext.naLogger
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.event.HandlesEvent

class BattleManager(
    private val eventBus: EventBus,
    registry: RunServiceRegistry,
    private val runStateService: RunStateService,
    private val attributeService: AttributeService,
    private val randomizer: RandomizerService,
    private val skillService: SkillService
) : RunService(eventBus, registry) {
    private val logger = naLogger()
    private lateinit var context: BattleContext

    private fun getActiveAnomalies(): Iterable<Anomaly> {
        val state = runStateService.load()

        val anomalies = mutableListOf<Anomaly>()
        anomalies += state.anomaly
        anomalies += state.enemies

        return anomalies.toList()
    }

    fun startBattle() {
        logger.info { "Battle start" }
        val state = runStateService.load()
        state.enemies.forEach(attributeService::performInitialAttributeCalculation)

        context = BattleContext(state.anomaly, state.enemies)
        nextTurn()
    }

    fun nextTurn() {
        logger.info { "Turn start" }
        val anomalies = getActiveAnomalies().sortedWith(Comparator.comparing<Anomaly?, Int?> { it.turnDelay }.thenByDescending { it.tdr() })
        val nextAnomaly = anomalies.first()
        val lowestTurnDelay = nextAnomaly.turnDelay
        logger.info { "Next turn delay was $lowestTurnDelay" }
        anomalies.forEach { it.turnDelay -= lowestTurnDelay }
        context.currentTurn = nextAnomaly

        runStateService.intercept(InterceptorHook.TURN_START, context)

        if (nextAnomaly == runStateService.load().anomaly) {
            logger.info { "Starting player turn" }
            eventBus.enqueueEventSync(PlayerTurnReady())
        } else {
            logger.info { "Starting AI turn" }
            processAiTurn()
        }
    }

    @HandlesEvent(TurnComplete::class)
    fun finalizeTurn() {
        logger.info { "Finalizing turn" }
        runStateService.intercept(InterceptorHook.TURN_END, context)

        val runState = runStateService.load()

        if (!runState.anomaly.isAlive) {
            logger.info { "Player dead, run over" }
            eventBus.enqueueEventSync(RunEndEvent(false))
            return
        } else if (runState.enemies.isEmpty()) {
            logger.info { "All enemies dead, battle complete" }
            eventBus.enqueueEventSync(BattleComplete())
            return
        }

        // TODO: Any other cleanup that needs to be done here?
        nextTurn()
    }

    @HandlesEvent
    fun hpChanged(event: HpChanged) {
        val anomaly = event.self
        if (!anomaly.isPlayer && !anomaly.isAlive) {
            runStateService.update {
                enemies -= anomaly
            }
        }
    }

    private fun processAiTurn() {
        val enemy = context.currentTurn
        val skill = randomizer.randomizeBasic { enemy.currentSkills.random(it) }
        skillService.useSkill(skill, context.currentTurn, listOf(runStateService.load().anomaly))
        eventBus.enqueueEventSync(TurnComplete())
    }
}

class BattleContext(val player: Anomaly, val enemies: List<Anomaly>) : InterceptableAdapter() {
    lateinit var currentTurn: Anomaly
}