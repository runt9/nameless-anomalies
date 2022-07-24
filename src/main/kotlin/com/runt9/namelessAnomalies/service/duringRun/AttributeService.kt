package com.runt9.namelessAnomalies.service.duringRun

import com.runt9.namelessAnomalies.model.anomaly.Anomaly
import com.runt9.namelessAnomalies.model.anomaly.maxHp
import com.runt9.namelessAnomalies.model.attribute.Attribute
import com.runt9.namelessAnomalies.model.attribute.AttributeModifier
import com.runt9.namelessAnomalies.model.attribute.AttributePriority
import com.runt9.namelessAnomalies.model.attribute.AttributeType
import com.runt9.namelessAnomalies.model.attribute.AttributeType.BASE_DAMAGE
import com.runt9.namelessAnomalies.model.attribute.AttributeType.BODY
import com.runt9.namelessAnomalies.model.attribute.AttributeType.COOLDOWN_REDUCTION
import com.runt9.namelessAnomalies.model.attribute.AttributeType.CRIT_CHANCE
import com.runt9.namelessAnomalies.model.attribute.AttributeType.CRIT_MULTI
import com.runt9.namelessAnomalies.model.attribute.AttributeType.DAMAGE_RESISTANCE
import com.runt9.namelessAnomalies.model.attribute.AttributeType.DODGE_CHANCE
import com.runt9.namelessAnomalies.model.attribute.AttributeType.INSTINCT
import com.runt9.namelessAnomalies.model.attribute.AttributeType.LUCK
import com.runt9.namelessAnomalies.model.attribute.AttributeType.MAX_HP
import com.runt9.namelessAnomalies.model.attribute.AttributeType.MIND
import com.runt9.namelessAnomalies.model.attribute.AttributeType.TURN_DELAY_REDUCTION
import com.runt9.namelessAnomalies.model.attribute.priority
import com.runt9.namelessAnomalies.model.event.AttributesUpdated
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import kotlin.math.floor
import kotlin.math.roundToInt

class AttributeService(private val eventBus: EventBus, registry: RunServiceRegistry) : RunService(eventBus, registry) {
    fun performInitialAttributeCalculation(anomaly: Anomaly) {
        doRecalculate(anomaly)
        anomaly.currentHp = anomaly.maxHp().roundToInt()
        eventBus.enqueueEventSync(AttributesUpdated(anomaly))
    }

    fun recalculateAttrs(anomaly: Anomaly) {
        doRecalculate(anomaly)
        eventBus.enqueueEventSync(AttributesUpdated(anomaly))
    }

    private fun doRecalculate(anomaly: Anomaly) {
        anomaly.attrs.filter { it.key.priority == AttributePriority.PRIMARY }.forEach { (type, attr) ->
            attr.attrModsFromLevels.clear()
            applyAttrModsFromLevels(type, attr, anomaly)
            attr.recalculate()
        }

        anomaly.attrs.filter { it.key.priority == AttributePriority.SECONDARY }.forEach { (type, attr) ->
            attr.attrModsFromAttrs.clear()
            applyAttrModFromPrimary(type, attr, anomaly.attrs.filter { it.key.priority == AttributePriority.PRIMARY })
            attr.recalculate()
        }

        anomaly.attrs.filter { it.key.priority == AttributePriority.ADDITIONAL }.values.forEach(Attribute::recalculate)

        if (anomaly.maxHp() < anomaly.currentHp) {
            anomaly.currentHp = anomaly.maxHp().roundToInt()
        }
    }

    private fun applyAttrModsFromLevels(type: AttributeType, attr: Attribute, anomaly: Anomaly) {
        anomaly.definition.attrsPerLevel[type]?.also { amt ->
            val totalAmt = amt * anomaly.level
            attr.addAttrModFromLevels(AttributeModifier(type, flatModifier = totalAmt))
        }
    }

    // TODO: Probably could use a refactor
    private fun applyAttrModFromPrimary(type: AttributeType, attr: Attribute, primaryAttrs: Map<AttributeType, Attribute>) {
        when (type) {
            MAX_HP -> {
                val flat = floor(primaryAttrs[BODY]!!.invoke())
                val percent = floor(primaryAttrs[MIND]!!.invoke() / 2f)
                attr.addAttrModFromAttrs(AttributeModifier(MAX_HP, flat, percent))
            }
            DAMAGE_RESISTANCE -> {
                val flat = floor(primaryAttrs[BODY]!!.invoke() / 10f) / 100f
                val percent = floor(primaryAttrs[LUCK]!!.invoke() / 25f)
                attr.addAttrModFromAttrs(AttributeModifier(DAMAGE_RESISTANCE, flat, percent))
            }
            DODGE_CHANCE -> {
                val flat = floor(primaryAttrs[MIND]!!.invoke() / 50f) / 100f
                val percent = floor(primaryAttrs[LUCK]!!.invoke() / 5f)
                attr.addAttrModFromAttrs(AttributeModifier(DODGE_CHANCE, flat, percent))
            }
            TURN_DELAY_REDUCTION -> {
                val flat = floor(primaryAttrs[INSTINCT]!!.invoke() / 10f) / 100f
                val percent = floor(primaryAttrs[BODY]!!.invoke() / 25f)
                attr.addAttrModFromAttrs(AttributeModifier(TURN_DELAY_REDUCTION, flat, percent))
            }
            BASE_DAMAGE -> {
                val flat = floor(primaryAttrs[INSTINCT]!!.invoke())
                val percent = floor(primaryAttrs[BODY]!!.invoke() / 2f)
                attr.addAttrModFromAttrs(AttributeModifier(BASE_DAMAGE, flat, percent))
            }
            CRIT_CHANCE -> {
                val flat = floor(primaryAttrs[LUCK]!!.invoke() / 50f) / 100f
                val percent = floor(primaryAttrs[INSTINCT]!!.invoke() / 5f)
                attr.addAttrModFromAttrs(AttributeModifier(CRIT_CHANCE, flat, percent))
            }
            CRIT_MULTI -> {
                val flat = floor(primaryAttrs[LUCK]!!.invoke() / 2f) / 100f
                val percent = floor(primaryAttrs[MIND]!!.invoke() / 5f)
                attr.addAttrModFromAttrs(AttributeModifier(CRIT_MULTI, flat, percent))
            }
            COOLDOWN_REDUCTION -> {
                val flat = floor(primaryAttrs[MIND]!!.invoke() / 10f) / 100f
                val percent = floor(primaryAttrs[INSTINCT]!!.invoke() / 25f)
                attr.addAttrModFromAttrs(AttributeModifier(COOLDOWN_REDUCTION, flat, percent))
            }
            else -> {}
        }
    }
}
