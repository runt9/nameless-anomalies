package com.runt9.namelessAnomalies.util.ext

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

const val PERCENT_MULTI = 100

fun Float.percent() = this * PERCENT_MULTI
fun Double.percent() = this * PERCENT_MULTI
fun Double.sqrt() = sqrt(this)
fun Float.sqrt() = sqrt(this)
fun Int.sqrt() = toDouble().sqrt()

fun ClosedRange<Float>.random(rng: Random) = rng.nextFloat() * (endInclusive - start) + start

fun Float.displayInt() = roundToInt().toString()
fun Float.displayDecimal(decimals: Int = 2) = "%.${decimals}f".format(this)
fun Float.displayMultiplier(decimals: Int = 2) = "${displayDecimal(decimals)}x"
fun Float.displayPercent(decimals: Int = 1) = "${(this * 100f).displayDecimal(decimals)}%"

fun Float.clamp(min: Float? = null, max: Float? = null) = when {
    min != null && min > this -> min
    max != null && max < this -> max
    else -> this
}

data class Size(val width: Float, val height: Float)

val Float.radDeg get() = this * MathUtils.radDeg
val Float.degRad get() = this * MathUtils.degRad
fun Vector2.toAngle() = atan2(-x.toDouble(), y.toDouble()).toFloat()

fun Float.toVector(outVector: Vector2): Vector2 {
    outVector.x = (-sin(toDouble())).toFloat()
    outVector.y = cos(toDouble()).toFloat()
    return outVector
}


fun Boolean.toInt() = if (this) 1 else 0
