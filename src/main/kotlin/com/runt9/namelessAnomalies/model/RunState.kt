package com.runt9.namelessAnomalies.model

import com.runt9.namelessAnomalies.model.anomaly.Anomaly
import com.runt9.namelessAnomalies.model.graph.MapGraph
import com.runt9.namelessAnomalies.model.graph.node.Node
import com.runt9.namelessAnomalies.model.graph.node.RootNode
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
    var cells: Int = 0,
    var dnaPoints: Int = 0,
    var currentMap: MapGraph = MapGraph(RootNode()),
    var floor: Int = 1,
    var currentNode: Node = RootNode(),
    var inBattle: Boolean = false,
    override var interceptors: Map<InterceptorHook, MutableList<Interceptor<InterceptableContext>>> = mapOf(),
    var enemies: List<Anomaly> = listOf()
) : InterceptableContext
