package com.runt9.namelessAnomalies.view.duringRun

import com.badlogic.gdx.ai.Timepiece
import com.runt9.namelessAnomalies.model.event.RunEndEvent
import com.runt9.namelessAnomalies.model.event.enqueueShowDialog
import com.runt9.namelessAnomalies.service.duringRun.RunInitializer
import com.runt9.namelessAnomalies.service.duringRun.RunServiceRegistry
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.event.HandlesEvent
import com.runt9.namelessAnomalies.util.framework.ui.core.GameScreen
import com.runt9.namelessAnomalies.view.duringRun.game.DuringRunGameController
import com.runt9.namelessAnomalies.view.duringRun.ui.DuringRunUiController
import com.runt9.namelessAnomalies.view.duringRun.ui.runEnd.RunEndDialogController
import ktx.async.onRenderingThread

class DuringRunScreen(
    override val gameController: DuringRunGameController,
    override val uiController: DuringRunUiController,
    private val eventBus: EventBus,
    private val aiTimepiece: Timepiece,
    private val runInitializer: RunInitializer,
    private val runServiceRegistry: RunServiceRegistry,
) : GameScreen(GAME_AREA_WIDTH, GAME_AREA_HEIGHT) {
    @HandlesEvent
    suspend fun runEnd(event: RunEndEvent) = onRenderingThread {
        // TODO: Need a proper clear of everything
        eventBus.enqueueShowDialog<RunEndDialogController>()
    }

    override fun show() {
        eventBus.registerHandlers(this)
        runInitializer.initialize()

        super.show()
    }

    override fun hide() {
        eventBus.unregisterHandlers(this)
        runInitializer.dispose()
        super.hide()
    }
}
