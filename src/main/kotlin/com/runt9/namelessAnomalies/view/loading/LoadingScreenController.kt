package com.runt9.namelessAnomalies.view.loading

import com.runt9.namelessAnomalies.model.event.AssetsLoadedEvent
import com.runt9.namelessAnomalies.model.event.enqueueChangeScreen
import com.runt9.namelessAnomalies.util.ext.percent
import com.runt9.namelessAnomalies.util.ext.naLogger
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.event.HandlesEvent
import com.runt9.namelessAnomalies.util.framework.ui.controller.UiScreenController
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView
import com.runt9.namelessAnomalies.view.mainMenu.MainMenuScreenController
import ktx.assets.async.AssetStorage
import ktx.async.onRenderingThread

class LoadingScreenController(private val assets: AssetStorage, private val eventBus: EventBus) : UiScreenController() {
    private val logger = naLogger()
    override val vm = LoadingScreenViewModel()
    override val view = injectView<LoadingScreenView>()

    override fun show() {
        super.show()
        eventBus.registerHandlers(this)
    }

    override fun render(delta: Float) {
        super.render(delta)
        assets.progress.run {
            logger.debug { "Asset loading status: $loaded / $total (${percent.percent()}%)" }
            vm.loadingPercent(percent)
        }
    }

    override fun hide() {
        super.hide()
        eventBus.unregisterHandlers(this)
    }

    @HandlesEvent
    @Suppress("UnusedPrivateMember")
    suspend fun handle(event: AssetsLoadedEvent) = onRenderingThread {
        logger.debug { "Loading complete, moving to main menu" }
        eventBus.enqueueChangeScreen<MainMenuScreenController>()
    }
}
