package com.runt9.namelessAnomalies.view.mainMenu

import com.runt9.namelessAnomalies.model.event.enqueueChangeScreen
import com.runt9.namelessAnomalies.model.event.enqueueExitRequest
import com.runt9.namelessAnomalies.model.event.enqueueShowDialog
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.ui.controller.UiScreenController
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView
import com.runt9.namelessAnomalies.util.framework.ui.viewModel.emptyViewModel
import com.runt9.namelessAnomalies.view.anomalySelect.AnomalySelectController
import com.runt9.namelessAnomalies.view.settings.SettingsDialogController

class MainMenuScreenController(private val eventBus: EventBus) : UiScreenController() {
    override val vm = emptyViewModel()
    override val view = injectView<MainMenuView>()

    fun newRun() {
        eventBus.enqueueChangeScreen<AnomalySelectController>()
    }

    fun showSettings() = eventBus.enqueueShowDialog<SettingsDialogController>()
    fun exit() = eventBus.enqueueExitRequest()
}
