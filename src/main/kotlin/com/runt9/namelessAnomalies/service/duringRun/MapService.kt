package com.runt9.namelessAnomalies.service.duringRun

import com.runt9.namelessAnomalies.model.graph.Connection
import com.runt9.namelessAnomalies.model.graph.MapGraph
import com.runt9.namelessAnomalies.model.graph.node.BattleRoom
import com.runt9.namelessAnomalies.model.graph.node.Node
import com.runt9.namelessAnomalies.model.graph.node.RootNode
import com.runt9.namelessAnomalies.service.RandomizerService
import com.runt9.namelessAnomalies.util.ext.generateWeightedList
import com.runt9.namelessAnomalies.util.ext.naLogger
import com.runt9.namelessAnomalies.util.ext.toInt
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.roundToInt

private const val MIN_DISTANCE_FROM_ENTRANCE = 2
private const val MAX_DISTANCE_FROM_ENTRANCE = 6
private const val MIN_NODES_PER_DIRECTION = 4
private const val MAX_NODES_PER_DIRECTION = 8
private const val MIN_NODES_TO_GENERATE = 15
private const val MAX_NODES_TO_GENERATE = 20

class MapService(
    private val randomizer: RandomizerService,
    private val battleManager: BattleManager,
    private val runStateService: RunStateService,
    eventBus: EventBus,
    registry: RunServiceRegistry
) : RunService(eventBus, registry) {
    private val logger = naLogger()

    fun generateMap(): MapGraph {
        val entrance = RootNode()
        val mapGraph = MapGraph(entrance)

        val nodesGenerated = AtomicInteger(0)
        // Always generate 3 connections for the root node
        repeat(3) {
            val room = BattleRoom(1)
            nodesGenerated.incrementAndGet()
            val nodesGeneratedThisDirection = AtomicInteger(1)
            generatePath(room, 1, nodesGenerated, nodesGeneratedThisDirection)
            val connection = Connection(entrance, room).apply { explored = true }
            entrance.outgoingConnections += connection

            logger.info { "Finished initial branch generation, generated ${nodesGeneratedThisDirection.get()} nodes" }
        }

        while (mapGraph.nodeCount < MIN_NODES_TO_GENERATE) {
            val leafNode = getClosestLeafNode(mapGraph)
            logger.info { "Not at minimum, generating more from node distance ${leafNode.distanceFromEntrance}" }
            generatePath(leafNode, leafNode.distanceFromEntrance + 1, nodesGenerated, AtomicInteger(0))
        }

        logger.info { "Finished generating map, generated ${mapGraph.nodeCount} nodes" }

        return mapGraph
    }

    private fun getClosestLeafNode(mapGraph: MapGraph) =
        mapGraph.allNodes.filter { it.outgoingConnections.isEmpty() }.minByOrNull { it.distanceFromEntrance }!!

    private fun generatePath(fromRoom: Node, distanceFromEntrance: Int, nodesGenerated: AtomicInteger, nodesGeneratedThisDirection: AtomicInteger) {
        val weights = mutableMapOf<Int, Int>()

        val hitMinimumTotal = nodesGenerated.get() >= MIN_NODES_TO_GENERATE
        val hitMinimumDistance = distanceFromEntrance >= MIN_DISTANCE_FROM_ENTRANCE
        val hitMinimumNodesDirection = nodesGeneratedThisDirection.get() >= MIN_NODES_PER_DIRECTION

        val branchWeight = hitMinimumTotal.toInt() + hitMinimumDistance.toInt() + hitMinimumNodesDirection.toInt()

        weights[1] = (MAX_DISTANCE_FROM_ENTRANCE - distanceFromEntrance) * (4 - branchWeight)
        weights[2] = (distanceFromEntrance / 2f).roundToInt() * (4 - branchWeight)
        weights[3] = (distanceFromEntrance / 4f).roundToInt() * (4 - branchWeight)

        if (nodesGeneratedThisDirection.get() >= MIN_NODES_PER_DIRECTION) {
            weights[0] = distanceFromEntrance * branchWeight
        }

        val random = randomizer.randomizeBasic { generateWeightedList(weights).random(it) }
        if (random == 0) return

        repeat(random) {
            if (nodesGenerated.get() >= MAX_NODES_TO_GENERATE) {
                logger.info { "Max nodes hit, done generating" }
                return@generatePath
            }

            if (distanceFromEntrance >= MAX_DISTANCE_FROM_ENTRANCE) {
                logger.info { "Max distance hit, done with this branch" }
                return@generatePath
            }

            if (nodesGeneratedThisDirection.get() >= MAX_NODES_PER_DIRECTION) {
                logger.info { "Max nodes this direction hit, done with this branch" }
                return@generatePath
            }

            val nextRoom = BattleRoom(distanceFromEntrance)
            nodesGenerated.incrementAndGet()
            nodesGeneratedThisDirection.incrementAndGet()
            fromRoom.outgoingConnections += Connection(fromRoom, nextRoom)
            generatePath(nextRoom, distanceFromEntrance + 1, nodesGenerated, nodesGeneratedThisDirection)
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

        runStateService.update {
            currentNode = node
        }
    }
}
