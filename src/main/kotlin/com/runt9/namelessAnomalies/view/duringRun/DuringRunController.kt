package com.runt9.namelessAnomalies.view.duringRun

import com.badlogic.gdx.utils.Disposable
import com.runt9.namelessAnomalies.model.anomaly.Anomaly
import com.runt9.namelessAnomalies.model.event.BattleComplete
import com.runt9.namelessAnomalies.model.event.PlayerTurnReady
import com.runt9.namelessAnomalies.model.event.RunEndEvent
import com.runt9.namelessAnomalies.model.event.RunStateUpdated
import com.runt9.namelessAnomalies.model.event.SkillSelected
import com.runt9.namelessAnomalies.model.event.TurnComplete
import com.runt9.namelessAnomalies.model.event.enqueueShowDialog
import com.runt9.namelessAnomalies.model.skill.Skill
import com.runt9.namelessAnomalies.model.skill.SkillTargetType
import com.runt9.namelessAnomalies.service.MapGenerator
import com.runt9.namelessAnomalies.service.duringRun.AttributeService
import com.runt9.namelessAnomalies.service.duringRun.BattleManager
import com.runt9.namelessAnomalies.service.duringRun.RunInitializer
import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.service.duringRun.SkillService
import com.runt9.namelessAnomalies.util.ext.loadTexture
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.event.HandlesEvent
import com.runt9.namelessAnomalies.util.framework.ui.controller.BasicScreenController
import com.runt9.namelessAnomalies.util.framework.ui.controller.Controller
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView
import com.runt9.namelessAnomalies.view.duringRun.enemy.EnemyViewModel
import com.runt9.namelessAnomalies.view.duringRun.ui.runEnd.RunEndDialogController
import ktx.assets.async.AssetStorage
import ktx.async.onRenderingThread

class DuringRunController(
    private val eventBus: EventBus,
    private val runInitializer: RunInitializer,
    private val runStateService: RunStateService,
    private val assets: AssetStorage,
    private val skillService: SkillService,
    private val battleManager: BattleManager,
    private val attributeService: AttributeService,
    private val mapGenerator: MapGenerator
) : BasicScreenController() {
    override val vm = DuringRunViewModel()
    override val view = injectView<DuringRunView>()
    private val children = mutableListOf<Controller>()
    private var selectedSkill: Skill? = null
    private lateinit var player: Anomaly

    @HandlesEvent
    suspend fun runEnd(event: RunEndEvent) = onRenderingThread {
        // TODO: Need a proper clear of everything
        eventBus.enqueueShowDialog<RunEndDialogController>()
    }

    @HandlesEvent
    suspend fun runStateUpdated(event: RunStateUpdated) = onRenderingThread {
        val newState = event.newState
        vm.enemies(newState.enemies.map(::EnemyViewModel))
    }

    @HandlesEvent
    suspend fun skillSelected(event: SkillSelected) = onRenderingThread {
        selectedSkill = event.skill
    }

    @HandlesEvent(TurnComplete::class)
    suspend fun turnComplete() = onRenderingThread {
        vm.isPlayersTurn(false)
    }

    @HandlesEvent
    suspend fun playerReady(event: PlayerTurnReady) = onRenderingThread {
        vm.isPlayersTurn(true)
    }

    @HandlesEvent(BattleComplete::class)
    suspend fun battleComplete() = onRenderingThread {
        // TODO: Do cleanup, show loot menu, and allow moving to the next room
    }

    override fun load() {
        eventBus.registerHandlers(this)
        runInitializer.initialize()
        runStateService.load().apply {
            player = anomaly
            vm.anomaly(assets.loadTexture(anomaly.definition.texture))
            vm.enemies(enemies.map(::EnemyViewModel))
        }
        attributeService.performInitialAttributeCalculation(player)

        runStateService.update {
            currentMap = mapGenerator.generateMap()
        }

        // TODO: This should come from selecting the first node to go to
        battleManager.startBattle()
    }

    override fun dispose() {
        children.forEach(Disposable::dispose)
        runInitializer.dispose()
        eventBus.unregisterHandlers(this)
        super.dispose()
    }

    fun addChild(controller: Controller) = children.add(controller)

    fun enemyClicked(enemy: EnemyViewModel) {
        if (selectedSkill == null) return

        val skill = selectedSkill!!

        if (skill.definition.target == SkillTargetType.SINGLE) {
            skillService.useSkill(skill, player, listOf(enemy.enemy))
            eventBus.enqueueEventSync(TurnComplete())
            selectedSkill = null
        }
    }
}
