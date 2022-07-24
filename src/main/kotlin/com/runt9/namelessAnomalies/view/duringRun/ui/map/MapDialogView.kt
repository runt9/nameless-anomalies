package com.runt9.namelessAnomalies.view.duringRun.ui.map

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisImage
import com.runt9.namelessAnomalies.model.graph.Connection
import com.runt9.namelessAnomalies.service.RandomizerService
import com.runt9.namelessAnomalies.util.ext.degRad
import com.runt9.namelessAnomalies.util.ext.naLogger
import com.runt9.namelessAnomalies.util.ext.toVector
import com.runt9.namelessAnomalies.util.ext.ui.circlePixmapTexture
import com.runt9.namelessAnomalies.util.ext.ui.rectPixmapTexture
import com.runt9.namelessAnomalies.util.framework.ui.view.DialogView
import ktx.actors.centerPosition
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.scene2d.KTable
import ktx.scene2d.textButton
import ktx.scene2d.vis.KFloatingGroup
import ktx.scene2d.vis.floatingGroup
import ktx.scene2d.vis.visImage
import kotlin.math.roundToInt

private const val CONNECTION_LENGTH = 60f
private const val NODE_RADIUS = 10f

// TODO: Allow moving and zooming the map
class MapDialogView(
    override val controller: MapDialogController,
    override val vm: MapDialogViewModel,
    private val randomizer: RandomizerService
) : DialogView(controller, "Map") {
    private val logger = naLogger()

    override val widthScale: Float = 0.75f
    override val heightScale: Float = 0.9f

    override fun KTable.initContentTable() {
        floatingGroup {
            setOrigin(Align.center)
            setFillParent(true)
            pack()

            var collided = true
            while (collided) {
                clear()
                val rootNode = visImage(circlePixmapTexture(NODE_RADIUS.roundToInt(), true, Color.FOREST)) {
                    centerPosition(this@floatingGroup.width, this@floatingGroup.height)
                    zIndex = 2
                }

                val map = vm.map.get()
                val allNodePositions = mutableListOf(Vector2(rootNode.x, rootNode.y))

                collided = doConnections(map.entrance.outgoingConnections, allNodePositions, rootNode, 0f, true)
                if (collided) {
                    logger.info { "Collision detected, redrawing" }
                }

                rootNode.toFront()
            }
        }.cell(grow = true, align = Align.center)

        controller.finishedDrawing()
    }

    private fun KFloatingGroup.doConnections(
        connections: Collection<Connection>,
        allNodePositions: MutableList<Vector2>,
        previousNode: VisImage,
        previousAngle: Float,
        isRoot: Boolean = false
    ): Boolean {
        val angleBetween = (if (isRoot) 360f else 180f) / connections.size
        val randomAngle = (if (connections.size == 1) listOf(-45f, 45f, 0f).random(randomizer.rng) else (45f * (connections.size - 1)))
        var currentAngle = previousAngle - randomAngle

        connections.forEach { c ->
            visImage(rectPixmapTexture(CONNECTION_LENGTH.toInt(), 2, if (c.explored) Color.WHITE else Color.GRAY)) {
                if (!vm.hasDrawnOnce.get()) {
                    c.position = Vector2(previousNode.x + NODE_RADIUS, previousNode.y + NODE_RADIUS)
                    c.rotation = currentAngle
                }

                zIndex = 1
                setPosition(c.position.x, c.position.y)
                rotation = c.rotation
            }

            val nextNode = c.toNode

            val nodeColor = when {
                nextNode.isEmpty -> Color.DARK_GRAY
                nextNode.hasTravelled -> Color.GRAY
                else -> Color.WHITE
            }

            val nextNodeImg = visImage(circlePixmapTexture(NODE_RADIUS.toInt(), true, nodeColor)) {
                val nodePos = Vector2(previousNode.x, previousNode.y).mulAdd((currentAngle - 90f).degRad.toVector(Vector2()), CONNECTION_LENGTH)

                if (!vm.hasDrawnOnce.get()) {
                    if (allNodePositions.any { it.dst(nodePos) <= NODE_RADIUS * 3 }) {
                        return@doConnections true
                    }

                    nextNode.position = nodePos
                }

                allNodePositions += nodePos

                zIndex = 2
                setPosition(nextNode.position.x, nextNode.position.y)

                onClick {
                    controller.nodeClicked(nextNode, c)
                }
            }

            if (nextNode.outgoingConnections.isNotEmpty()) {
                val collided = doConnections(nextNode.outgoingConnections, allNodePositions, nextNodeImg, currentAngle)
                if (collided) return@doConnections true
            }

            currentAngle += angleBetween
        }

        return false
    }

    override fun KTable.initButtons() {
        textButton("Close", "round") { onChange { controller.close() } }.cell(row = true, spaceBottom = 5f)
    }
}
