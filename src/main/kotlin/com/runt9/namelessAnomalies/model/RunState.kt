package com.runt9.namelessAnomalies.model

import com.runt9.namelessAnomalies.model.anomaly.Anomaly
import com.runt9.namelessAnomalies.model.anomaly.definition.prototypeEnemy
import com.runt9.namelessAnomalies.model.interceptor.InterceptableContext
import com.runt9.namelessAnomalies.model.interceptor.Interceptor
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook
import com.runt9.namelessAnomalies.util.ext.randomString
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class RunState(
    val seed: String = Random.randomString(8),
    val anomaly: Anomaly,
    val gold: Int = 0,
    val dnaPoints: Int = 0,
    val floor: Int = 1,
    val room: Int = 1,
    override val interceptors: MutableMap<InterceptorHook, MutableList<Interceptor<InterceptableContext>>> = mutableMapOf(),
    val enemies: MutableList<Anomaly> = mutableListOf(Anomaly(prototypeEnemy), Anomaly(prototypeEnemy))
) : InterceptableContext
