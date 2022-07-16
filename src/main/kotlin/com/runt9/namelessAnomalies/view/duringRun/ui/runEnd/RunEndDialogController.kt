package com.runt9.namelessAnomalies.view.duringRun.ui.runEnd

import com.runt9.namelessAnomalies.model.event.enqueueChangeScreen
import com.runt9.namelessAnomalies.model.event.enqueueExitRequest
import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.ui.controller.DialogController
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView
import com.runt9.namelessAnomalies.view.mainMenu.MainMenuScreenController

class RunEndDialogController(private val eventBus: EventBus, private val runStateService: RunStateService) : DialogController() {
    override val vm = RunEndDialogViewModel()
    override val view = injectView<RunEndDialogView>()

    override fun load() {
        runStateService.load().apply { vm.runWon(hp > 0) }
    }

    fun mainMenu() {
        eventBus.enqueueChangeScreen<MainMenuScreenController>()
        hide()
    }
    fun exit() = eventBus.enqueueExitRequest()
}
