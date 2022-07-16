package com.runt9.namelessAnomalies.util.framework.ui.controller

import com.runt9.namelessAnomalies.util.framework.ui.core.BasicStage
import com.runt9.namelessAnomalies.util.framework.ui.view.DialogView

abstract class DialogController : Controller {
    abstract override val view: DialogView
    private var stage: BasicStage? = null

    var isShown = false

    fun show(stage: BasicStage) {
        if (!isShown) {
            load()
            this.stage = stage
            view.show(stage)
            view.init()
            isShown = true
        }
    }

    fun hide() {
        if (isShown) {
            view.hide()
            isShown = false
            stage = null
            dispose()
        }
    }
}
