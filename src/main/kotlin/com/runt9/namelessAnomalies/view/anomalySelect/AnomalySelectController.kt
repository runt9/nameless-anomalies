package com.runt9.namelessAnomalies.view.anomalySelect

import com.runt9.namelessAnomalies.model.RunState
import com.runt9.namelessAnomalies.model.anomaly.Anomaly
import com.runt9.namelessAnomalies.model.anomaly.definition.availablePlayerAnomalies
import com.runt9.namelessAnomalies.model.event.enqueueChangeScreen
import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.util.ext.randomString
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.ui.controller.BasicScreenController
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView
import com.runt9.namelessAnomalies.view.duringRun.DuringRunController
import com.runt9.namelessAnomalies.view.mainMenu.MainMenuScreenController
import kotlin.random.Random

class AnomalySelectController(
    private val eventBus: EventBus,
    private val runStateService: RunStateService
) : BasicScreenController() {
    override val vm = AnomalySelectViewModel(availablePlayerAnomalies)
    override val view = injectView<AnomalySelectView>()

    fun back() = eventBus.enqueueChangeScreen<MainMenuScreenController>()

    fun startRun() {
        val anomaly = vm.selectedAnomaly.get()
        val seed = vm.seed.get().ifBlank { Random.randomString(8) }
        runStateService.save(RunState(seed, Anomaly(anomaly, true)))
        eventBus.enqueueChangeScreen<DuringRunController>()
    }
}
