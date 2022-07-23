package com.runt9.namelessAnomalies.model.graph.node

import com.runt9.namelessAnomalies.model.graph.Connection
import kotlinx.serialization.Serializable

@Serializable
abstract class Node {
    // TODO: Not sure I like this being a mutable list. It's not mutable once its generated, but we need to be able generate a node without its connections figured out yet, right?
    val outgoingConnections = mutableListOf<Connection>()
}
