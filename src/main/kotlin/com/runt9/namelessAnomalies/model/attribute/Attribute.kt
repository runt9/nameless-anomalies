package com.runt9.namelessAnomalies.model.attribute

import com.runt9.namelessAnomalies.util.ext.clamp
import kotlinx.serialization.Serializable

@Serializable
class Attribute(val type: AttributeType) {
    val attrMods = mutableListOf<AttributeModifier>()
    var realValue = 0f
        private set
    var dirty = true
        private set

    operator fun invoke() = realValue

    fun addAttributeModifier(attrMod: AttributeModifier) {
        attrMods += attrMod
        dirty = true
    }

    fun removeAttributeModifier(attrMod: AttributeModifier) {
        attrMods -= attrMod
        dirty = true
    }

    fun recalculate() {
        if (!dirty) return

        var totalFlat = 0f
        var totalPercent = 0f

        attrMods.forEach {
            totalFlat += it.flatModifier
            totalPercent += it.percentModifier
        }

        val newValue = ((baseValue + totalFlat) * (1 + (totalPercent / 100))).clamp(clamp.min, clamp.max)
        if (newValue != realValue) {
            realValue = newValue
            // TODO: Changed callback
        }

        dirty = false
    }

    fun clone(): Attribute {
        val attr = Attribute(type)
        attr.attrMods.addAll(attrMods)
        attr.recalculate()
        return attr
    }

    override fun toString() = type.getDisplayValue(realValue)
}
