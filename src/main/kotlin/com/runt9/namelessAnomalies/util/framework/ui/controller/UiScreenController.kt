package com.runt9.namelessAnomalies.util.framework.ui.controller

import com.runt9.namelessAnomalies.util.framework.ui.core.UiScreen

abstract class UiScreenController : Controller, UiScreen() {
    override val uiController: Controller get() = this

    override fun dispose() {
        super<Controller>.dispose()
    }
}
