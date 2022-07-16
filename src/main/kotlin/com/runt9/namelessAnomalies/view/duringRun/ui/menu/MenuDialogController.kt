package com.runt9.namelessAnomalies.view.duringRun.ui.menu

import com.runt9.namelessAnomalies.model.event.enqueueChangeScreen
import com.runt9.namelessAnomalies.model.event.enqueueExitRequest
import com.runt9.namelessAnomalies.model.event.enqueueShowDialog
import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.ui.controller.DialogController
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView
import com.runt9.namelessAnomalies.view.mainMenu.MainMenuScreenController
import com.runt9.namelessAnomalies.view.settings.SettingsDialogController

class MenuDialogController(private val eventBus: EventBus, private val runStateService: RunStateService) : DialogController() {
    override val vm = MenuDialogViewModel()
    override val view = injectView<MenuDialogView>()

    override fun load() {
        runStateService.load().apply { vm.runSeed(seed) }
    }

    fun resume() {
        hide()
    }
    fun settings() = eventBus.enqueueShowDialog<SettingsDialogController>()
    fun mainMenu() {
        eventBus.enqueueChangeScreen<MainMenuScreenController>()
        hide()
    }
    fun exit() = eventBus.enqueueExitRequest()
}
