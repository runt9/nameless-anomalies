package com.runt9.namelessAnomalies.view.duringRun.ui.loot

import com.runt9.namelessAnomalies.service.duringRun.BattleLoot
import com.runt9.namelessAnomalies.util.framework.ui.viewModel.ViewModel

class LootDialogViewModel : ViewModel() {
    lateinit var loot: BattleLoot
    val levelUp = Binding(false)
}
