package com.runt9.namelessAnomalies.view.duringRun.ui.map

import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.util.framework.ui.controller.DialogController
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView

class MapDialogController(private val runStateService: RunStateService) : DialogController() {
    override val vm = MapDialogViewModel()
    override val view = injectView<MapDialogView>()

    override fun load() {
        runStateService.load().apply {
            vm.map(currentMap)
        }
    }

    fun close() {
        hide()
    }
}
