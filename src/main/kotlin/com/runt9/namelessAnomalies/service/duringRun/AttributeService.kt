package com.runt9.namelessAnomalies.service.duringRun

import com.runt9.namelessAnomalies.model.anomaly.Anomaly
import com.runt9.namelessAnomalies.model.anomaly.maxHp
import com.runt9.namelessAnomalies.model.attribute.Attribute
import com.runt9.namelessAnomalies.model.event.AttributesUpdated
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import kotlin.math.roundToInt

// TODO: Handle updating attribute modifiers on secondary attributes from primary
// TODO: Handle updating current HP if max HP is now lower than the prior current hp
class AttributeService(private val eventBus: EventBus, registry: RunServiceRegistry) : RunService(eventBus, registry) {
    fun performInitialAttributeCalculation(anomaly: Anomaly) {
        anomaly.attrs.values.forEach(Attribute::recalculate)
        anomaly.currentHp = anomaly.maxHp().roundToInt()
        eventBus.enqueueEventSync(AttributesUpdated(anomaly))
    }

    fun recalculateAttrs(anomaly: Anomaly) {
        anomaly.attrs.values.forEach(Attribute::recalculate)
        eventBus.enqueueEventSync(AttributesUpdated(anomaly))
    }
}
