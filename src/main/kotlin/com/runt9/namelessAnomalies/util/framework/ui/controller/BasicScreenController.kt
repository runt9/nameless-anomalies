package com.runt9.namelessAnomalies.util.framework.ui.controller

import com.runt9.namelessAnomalies.util.framework.ui.core.BasicScreen

abstract class BasicScreenController : Controller, BasicScreen() {
    override val controller: Controller get() = this

    override fun dispose() {
        super<Controller>.dispose()
    }
}
