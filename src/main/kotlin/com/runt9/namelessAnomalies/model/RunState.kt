package com.runt9.namelessAnomalies.model

import com.runt9.namelessAnomalies.util.ext.randomString
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class RunState(
    val seed: String = Random.randomString(8),
    var maxHp: Int = 0,
    var hp: Int = 0
)
