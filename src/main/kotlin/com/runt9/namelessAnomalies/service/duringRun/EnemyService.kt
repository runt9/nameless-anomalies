package com.runt9.namelessAnomalies.service.duringRun

import com.runt9.namelessAnomalies.model.anomaly.Anomaly
import com.runt9.namelessAnomalies.model.anomaly.definition.availableEnemyAnomalies
import com.runt9.namelessAnomalies.model.attribute.AttributeModifier
import com.runt9.namelessAnomalies.service.RandomizerService
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import kotlin.math.floor

class EnemyService(
    private val randomizer: RandomizerService,
    private val attributeService: AttributeService,
    private val runStateService: RunStateService,
    eventBus: EventBus,
    registry: RunServiceRegistry
) : RunService(eventBus, registry) {
    fun generateEnemies(): List<Anomaly> {
        val enemiesToGenerate = randomizer.rng.nextInt(1, 4)
        val enemies = mutableListOf<Anomaly>()

        val state = runStateService.load()
        val floor = state.floor
        val distance = state.currentNode.distanceFromEntrance

        // TODO: Determine distance from entrance node and use that in calculations below

        val relativeEnemyStrength = (-50f + (25f * floor) + (3f * distance)) - (10f * enemiesToGenerate)
        val enemyLevel = floor(1 + (floor(distance / 2f)) + (5 * (floor - 1))).toInt()

        repeat(enemiesToGenerate) {
            val enemyDef = randomizer.randomizeBasic { availableEnemyAnomalies.random(it) }
            val enemy = Anomaly(enemyDef, false)

            enemy.attrs.forEach { (type, attr) -> attr.addAttributeModifier(AttributeModifier(type, percentModifier = relativeEnemyStrength)) }
            enemy.level = enemyLevel
            attributeService.performInitialAttributeCalculation(enemy)
            enemies += enemy
        }

        return enemies
    }
}
