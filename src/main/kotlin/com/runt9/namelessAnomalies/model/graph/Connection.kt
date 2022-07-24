package com.runt9.namelessAnomalies.model.graph

import com.badlogic.gdx.math.Vector2
import com.runt9.namelessAnomalies.model.graph.node.Node
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
class Connection(val fromNode: Node, val toNode: Node) {
    var explored = false
    @Contextual
    var position: Vector2 = Vector2.Zero
    var rotation = 0f
}
