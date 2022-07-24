package com.runt9.namelessAnomalies.service.duringRun

import com.runt9.namelessAnomalies.model.anomaly.Anomaly
import com.runt9.namelessAnomalies.util.framework.event.EventBus

class AnomalyService(
    eventBus: EventBus,
    registry: RunServiceRegistry,
    private val attributeService: AttributeService
) : RunService(eventBus, registry) {
    fun levelUpAnomaly(anomaly: Anomaly) {
        attributeService.recalculateAttrs(anomaly)
    }
}
