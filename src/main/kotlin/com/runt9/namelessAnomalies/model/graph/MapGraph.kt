package com.runt9.namelessAnomalies.model.graph

import com.runt9.namelessAnomalies.model.graph.node.Node
import kotlinx.serialization.Serializable

@Serializable
class MapGraph(val entrance: Node) {
    val allNodes: List<Node> get() = getAllNodes(entrance)
    val nodeCount: Int get() = allNodes.size
    var hasDrawnOnce = false

    private fun getAllNodes(fromNode: Node): List<Node> {
        val allNodes = mutableListOf<Node>()
        fromNode.outgoingConnections.forEach {
            allNodes.add(it.toNode)
            allNodes.addAll(getAllNodes(it.toNode))
        }
        return allNodes
    }
}
