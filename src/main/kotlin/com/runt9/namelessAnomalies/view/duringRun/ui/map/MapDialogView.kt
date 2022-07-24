package com.runt9.namelessAnomalies.view.duringRun.ui.map

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisImage
import com.runt9.namelessAnomalies.model.graph.Connection
import com.runt9.namelessAnomalies.service.RandomizerService
import com.runt9.namelessAnomalies.util.ext.degRad
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

class MapDialogView(
    override val controller: MapDialogController,
    override val vm: MapDialogViewModel,
    private val randomizer: RandomizerService
) : DialogView(controller, "Map") {
    override val widthScale: Float = 0.75f
    override val heightScale: Float = 0.9f

    // TODO: This can't be different every time the dialog is opened
    // TODO: Somehow during generation the map has to be aware of collisions and prevent it
    override fun KTable.initContentTable() {
        floatingGroup {
            setOrigin(Align.center)
            setFillParent(true)
            pack()

            val rootNode = visImage(circlePixmapTexture(12, true, Color.FOREST)) {
                centerPosition(this@floatingGroup.width, this@floatingGroup.height)
            }

            val map = vm.map.get()

            doConnections(map.entrance.outgoingConnections, rootNode, 0f, true)
        }.cell(grow = true, align = Align.center)
    }

    private fun KFloatingGroup.doConnections(connections: Collection<Connection>, previousNode: VisImage, previousAngle: Float, isRoot: Boolean = false) {
        val angleBetween = (if (isRoot) 360f else 180f) / connections.size
        val randomAngle = (if (connections.size == 1) listOf(-45f, 45f, 0f).random(randomizer.rng) else (45f * (connections.size - 1)))
        var currentAngle = previousAngle - randomAngle

        connections.forEach { c ->
            // TODO: Z-index nonsense to put this below the node
            visImage(rectPixmapTexture(40, 2, if (c.explored) Color.WHITE else Color.GRAY)) {
                setPosition(previousNode.x + 10, previousNode.y + 10)
                rotation = currentAngle
            }

            val nextNode = c.toNode

            val nodeColor = when {
                nextNode.isEmpty -> Color.DARK_GRAY
                nextNode.hasTravelled -> Color.GRAY
                else -> Color.WHITE
            }

            val nextNodeImg = visImage(circlePixmapTexture(10, true, nodeColor)) {
                val nodePos = Vector2(previousNode.x, previousNode.y).mulAdd((currentAngle - 90f).degRad.toVector(Vector2()), 40f)
                setPosition(nodePos.x, nodePos.y)

                onClick {
                    controller.nodeClicked(nextNode, c)
                }
            }

            if (nextNode.outgoingConnections.isNotEmpty()) {
                doConnections(nextNode.outgoingConnections, nextNodeImg, currentAngle)
            }

            currentAngle += angleBetween
        }
    }

    override fun KTable.initButtons() {
        textButton("Close", "round") { onChange { controller.close() } }.cell(row = true, spaceBottom = 5f)
    }
}
