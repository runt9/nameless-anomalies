package com.runt9.namelessAnomalies.view.anomalySelect

import com.runt9.namelessAnomalies.model.RunState
import com.runt9.namelessAnomalies.model.event.enqueueChangeScreen
import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.util.ext.randomString
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.ui.controller.UiScreenController
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView
import com.runt9.namelessAnomalies.view.duringRun.DuringRunScreen
import com.runt9.namelessAnomalies.view.mainMenu.MainMenuScreenController
import kotlin.random.Random

class AnomalySelectController(
    private val eventBus: EventBus,
    private val runStateService: RunStateService
) : UiScreenController() {
    override val vm = AnomalySelectViewModel()
    override val view = injectView<AnomalySelectView>()

    fun back() = eventBus.enqueueChangeScreen<MainMenuScreenController>()

    fun startRun() {
        val seed = vm.seed.get().ifBlank { Random.randomString(8) }
        runStateService.save(RunState(seed = seed))
        eventBus.enqueueChangeScreen<DuringRunScreen>()
    }
}
