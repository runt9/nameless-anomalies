package com.runt9.namelessAnomalies.model.graph

import com.runt9.namelessAnomalies.model.graph.node.Node
import kotlinx.serialization.Serializable

@Serializable
class Connection(val fromNode: Node, val toNode: Node) {
    var explored = false
}
