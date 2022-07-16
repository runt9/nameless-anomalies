package com.runt9.namelessAnomalies.view.duringRun.ui

import com.runt9.namelessAnomalies.util.framework.ui.view.ScreenView

class DuringRunUiView(
    override val controller: DuringRunUiController,
    override val vm: DuringRunUiViewModel
) : ScreenView() {
    override fun init() {
        super.init()

        val controller = controller
        val vm = vm
    }
}
