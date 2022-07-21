package com.runt9.namelessAnomalies.model.attribute

import com.runt9.namelessAnomalies.util.ext.FloatRange
import com.runt9.namelessAnomalies.util.ext.displayInt
import kotlin.math.roundToInt

enum class AttributePriority { PRIMARY, SECONDARY, ADDITIONAL }

class AttributeRandomRange(val flat: FloatRange, val percent: FloatRange)
class AttributeValueClamp(val min: Float? = null, val max: Float? = null)

interface AttributeDefinition {
    val priority: AttributePriority
    // TODO: Convert shortName into icon
    val shortName: String
    val displayName: String
    val rangeForRandomizer: AttributeRandomRange
    val clamp: AttributeValueClamp
    val baseValue: Float

    fun getDisplayValue(value: Float): String

    fun Float.displayInt() = roundToInt().toString()
    fun Float.displayDecimal(decimals: Int = 2) = "%.${decimals}f".format(this)
    fun Float.displayMultiplier() = "${displayDecimal()}x"
    fun Float.displayPercent(decimals: Int = 1) = "${displayDecimal(decimals)}%"
}

fun attributeDef(
    priority: AttributePriority,
    shortName: String,
    displayName: String,
    rangeForRandomizer: AttributeRandomRange,
    clamp: AttributeValueClamp = AttributeValueClamp(),
    baseValue: Float = 0f,
    displayFn: Float.() -> String
) = object : AttributeDefinition {
    override val priority = priority
    override val shortName = shortName
    override val displayName = displayName
    override val rangeForRandomizer = rangeForRandomizer
    override val clamp = clamp
    override val baseValue = baseValue

    override fun getDisplayValue(value: Float) = value.displayFn()
}

// TODO: Clean way to display effects on secondary attributes
// TODO: Add in primary effects on secondary
fun primaryAttr(shortName: String, displayName: String) = attributeDef(
    AttributePriority.PRIMARY,
    shortName,
    displayName,
    AttributeRandomRange(10f..20f, 5f..10f),
    AttributeValueClamp(min = 0f),
    10f,
    Float::displayInt
)

val AttributeType.priority get() = definition.priority
val Attribute.priority get() = type.priority
val AttributeType.shortName get() = definition.shortName
val Attribute.shortName get() = type.shortName
val AttributeType.displayName get() = definition.displayName
val Attribute.displayName get() = type.displayName
val AttributeType.clamp get() = definition.clamp
val Attribute.clamp get() = type.clamp
val AttributeType.baseValue get() = definition.baseValue
val Attribute.baseValue get() = type.baseValue

fun AttributeType.getDisplayValue(value: Float) = definition.getDisplayValue(value)
fun Attribute.getDisplayValue() = type.getDisplayValue(realValue)
