package com.runt9.namelessAnomalies.view.duringRun.game

import com.badlogic.gdx.utils.Disposable
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.ui.controller.Controller
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView

class DuringRunGameController(
    private val eventBus: EventBus
) : Controller {
    override val vm = DuringRunGameViewModel()
    override val view = injectView<DuringRunGameView>()
    private val children = mutableListOf<Controller>()

    override fun load() {
        eventBus.registerHandlers(this)
    }

    override fun dispose() {
        eventBus.unregisterHandlers(this)
        clearChildren()
        super.dispose()
    }

    private fun clearChildren() {
        children.forEach(Disposable::dispose)
        children.clear()
    }

    fun addChild(controller: Controller) = children.add(controller)
}
