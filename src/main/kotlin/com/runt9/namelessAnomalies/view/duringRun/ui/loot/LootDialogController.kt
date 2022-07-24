package com.runt9.namelessAnomalies.view.duringRun.ui.loot

import com.runt9.namelessAnomalies.model.event.PlayerUpdated
import com.runt9.namelessAnomalies.model.event.enqueueShowDialog
import com.runt9.namelessAnomalies.service.duringRun.LootService
import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.ui.controller.DialogController
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView
import com.runt9.namelessAnomalies.view.duringRun.ui.map.MapDialogController

class LootDialogController(
    private val lootService: LootService,
    private val eventBus: EventBus,
    private val runStateService: RunStateService
) : DialogController() {
    override val vm = LootDialogViewModel()
    override val view = injectView<LootDialogView>()

    override fun load() {
        lootService.generateBattleLoot().apply {
            vm.xp(xp)
            vm.cells(cells)
        }
    }

    fun gainXp() {
        val player = runStateService.load().anomaly
        if (player.gainXp(vm.xp.get())) {
            vm.levelUp(true)
        }

        eventBus.enqueueEventSync(PlayerUpdated(player))
    }

    fun gainCells() {
        runStateService.update {
            cells += vm.cells.get()
        }
    }

    fun levelUp() {
        runStateService.update {
            anomaly.gainLevel()
        }
    }

    fun done() {
        hide()
        eventBus.enqueueShowDialog<MapDialogController>()
    }
}
