package com.runt9.namelessAnomalies.service.duringRun

import com.runt9.namelessAnomalies.model.anomaly.Anomaly
import com.runt9.namelessAnomalies.model.anomaly.definition.prototypeEnemy
import com.runt9.namelessAnomalies.service.RandomizerService
import com.runt9.namelessAnomalies.util.framework.event.EventBus

class EnemyService(
    private val randomizer: RandomizerService,
    private val attributeService: AttributeService,
    eventBus: EventBus,
    registry: RunServiceRegistry
) : RunService(eventBus, registry) {
    fun generateEnemies(): List<Anomaly> {
        val enemy = Anomaly(prototypeEnemy, false)
        attributeService.performInitialAttributeCalculation(enemy)
        return mutableListOf(enemy)
    }
}
