package com.runt9.namelessAnomalies.view.duringRun.ui.loot

import com.runt9.namelessAnomalies.model.event.PlayerUpdated
import com.runt9.namelessAnomalies.model.event.enqueueShowDialog
import com.runt9.namelessAnomalies.service.duringRun.AnomalyService
import com.runt9.namelessAnomalies.service.duringRun.LootService
import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.ui.controller.DialogController
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView
import com.runt9.namelessAnomalies.view.duringRun.ui.loot.mutation.MutationSelectDialogController
import com.runt9.namelessAnomalies.view.duringRun.ui.map.MapDialogController

class LootDialogController(
    private val lootService: LootService,
    private val eventBus: EventBus,
    private val runStateService: RunStateService,
    private val anomalyService: AnomalyService
) : DialogController() {
    override val vm = LootDialogViewModel()
    override val view = injectView<LootDialogView>()

    override fun load() {
        lootService.generateBattleLoot().apply {
            vm.loot = this
        }
    }

    fun gainXp() {
        val player = runStateService.load().anomaly
        if (player.gainXp(vm.loot.xp)) {
            vm.levelUp(true)
        }

        eventBus.enqueueEventSync(PlayerUpdated(player))
    }

    fun gainCells() {
        runStateService.update {
            cells += vm.loot.cells
        }
    }

    fun chooseMutation() {
        eventBus.enqueueShowDialog<MutationSelectDialogController>(vm.loot)
    }

    fun levelUp() {
        anomalyService.levelUpAnomaly(runStateService.load().anomaly)
    }

    fun done() {
        hide()
        eventBus.enqueueShowDialog<MapDialogController>()
    }
}
