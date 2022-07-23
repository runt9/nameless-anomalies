package com.runt9.namelessAnomalies.service.duringRun

import com.runt9.namelessAnomalies.model.graph.Connection
import com.runt9.namelessAnomalies.model.graph.MapGraph
import com.runt9.namelessAnomalies.model.graph.node.BattleRoom
import com.runt9.namelessAnomalies.model.graph.node.Node
import com.runt9.namelessAnomalies.model.graph.node.RootNode
import com.runt9.namelessAnomalies.service.RandomizerService
import com.runt9.namelessAnomalies.util.framework.event.EventBus

class MapService(
    private val randomizer: RandomizerService,
    private val battleManager: BattleManager,
    eventBus: EventBus,
    registry: RunServiceRegistry
) : RunService(eventBus, registry) {
    // TODO: Always generate a certain number of rooms, maybe 15-20? 12-15?
    fun generateMap(): MapGraph {
        val entrance = RootNode()
        val mapGraph = MapGraph(entrance)

        // Always generate 3 connections for the root node
        repeat(3) {
            val room = BattleRoom()
            val random = randomizer.rng.nextInt(1, 4)

            repeat(random) {
                val nextRoom = BattleRoom()
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
            val nextRoom = BattleRoom()
            fromRoom.outgoingConnections += Connection(fromRoom, nextRoom)
            generatePath(nextRoom, random)
        }
    }

    fun travelToNode(node: Node) {
        when (node) {
            is BattleRoom -> {
                battleManager.startBattle()
            }
            is RootNode -> return
        }

        node.hasTravelled = true
        node.outgoingConnections.forEach { it.explored = true }
    }
}
