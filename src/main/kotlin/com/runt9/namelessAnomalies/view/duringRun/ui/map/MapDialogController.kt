package com.runt9.namelessAnomalies.view.duringRun.ui.map

import com.runt9.namelessAnomalies.model.graph.Connection
import com.runt9.namelessAnomalies.model.graph.node.Node
import com.runt9.namelessAnomalies.model.graph.node.RootNode
import com.runt9.namelessAnomalies.service.duringRun.MapService
import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.util.framework.ui.controller.DialogController
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView

class MapDialogController(private val runStateService: RunStateService, private val mapService: MapService) : DialogController() {
    override val vm = MapDialogViewModel()
    override val view = injectView<MapDialogView>()
    private var hasDrawnOnce = false

    override fun load() {
        runStateService.load().apply {
            vm.map(currentMap)
            vm.hasDrawnOnce(hasDrawnOnce)
        }
    }

    fun close() {
        hide()
    }

    fun finishedDrawing() {
        hasDrawnOnce = true
    }

    fun nodeClicked(nextNode: Node, incomingConnection: Connection) {
        if (!incomingConnection.explored) return
        if (runStateService.load().inBattle) return
        if (nextNode is RootNode) return
        if (nextNode.isEmpty) return

        mapService.travelToNode(nextNode)
        hide()
    }
}
