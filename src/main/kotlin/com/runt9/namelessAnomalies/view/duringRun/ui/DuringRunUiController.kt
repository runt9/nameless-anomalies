package com.runt9.namelessAnomalies.view.duringRun.ui

import com.badlogic.gdx.utils.Disposable
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.ui.controller.Controller
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView

class DuringRunUiController(private val eventBus: EventBus) : Controller {
    override val vm = DuringRunUiViewModel()
    override val view = injectView<DuringRunUiView>()
    private val children = mutableListOf<Controller>()

    override fun load() {
        eventBus.registerHandlers(this)
    }

    override fun dispose() {
        children.forEach(Disposable::dispose)
        eventBus.unregisterHandlers(this)
        super.dispose()
    }

    fun addChild(controller: Controller) = children.add(controller)
}
