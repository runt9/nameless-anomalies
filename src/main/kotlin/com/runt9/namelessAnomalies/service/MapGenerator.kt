package com.runt9.namelessAnomalies.service

import com.runt9.namelessAnomalies.model.graph.Connection
import com.runt9.namelessAnomalies.model.graph.MapGraph
import com.runt9.namelessAnomalies.model.graph.node.Node
import com.runt9.namelessAnomalies.model.graph.node.Room
import com.runt9.namelessAnomalies.model.graph.node.RootNode
import com.runt9.namelessAnomalies.service.duringRun.RunService
import com.runt9.namelessAnomalies.service.duringRun.RunServiceRegistry
import com.runt9.namelessAnomalies.util.framework.event.EventBus

// Not sure if this needs to be a RunService or not
class MapGenerator(private val randomizer: RandomizerService, eventBus: EventBus, registry: RunServiceRegistry) : RunService(eventBus, registry) {
    fun generateMap(): MapGraph {
        val entrance = RootNode()
        val mapGraph = MapGraph(entrance)

        // Always generate 3 connections for the root node
        repeat(3) {
            val room = Room()
            val random = randomizer.rng.nextInt(1, 4)

            repeat(random) {
                val nextRoom = Room()
                room.outgoingConnections += Connection(room, nextRoom)
                generatePath(nextRoom, 3)
            }

            val connection = Connection(entrance, room).apply { explored = true }
            entrance.outgoingConnections += connection
        }

        return mapGraph
    }

    private fun generatePath(fromRoom: Node, previousNumBranches: Int) {
        val random = randomizer.rng.nextInt(0, previousNumBranches)
        if (random == 0) return

        repeat(random) {
            val nextRoom = Room()
            fromRoom.outgoingConnections += Connection(fromRoom, nextRoom)
            generatePath(nextRoom, random)
        }
    }
}
