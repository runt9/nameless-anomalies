package com.runt9.namelessAnomalies.model.graph.node

import com.badlogic.gdx.math.Vector2
import com.runt9.namelessAnomalies.model.graph.Connection
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
sealed class Node(val distanceFromEntrance: Int) {
    // TODO: Not sure I like this being a mutable list. It's not mutable once its generated, but we need to be able generate a node without its connections figured out yet, right?
    val outgoingConnections = mutableListOf<Connection>()
    var isEmpty = false
    var hasTravelled = false
    @Contextual
    var position: Vector2 = Vector2.Zero
}
