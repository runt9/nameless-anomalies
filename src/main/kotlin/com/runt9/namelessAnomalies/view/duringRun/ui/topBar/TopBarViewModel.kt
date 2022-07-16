package com.runt9.namelessAnomalies.view.duringRun.ui.topBar

import com.runt9.namelessAnomalies.util.framework.ui.viewModel.ViewModel

class TopBarViewModel : ViewModel() {
    val hp = Binding(0)
    val maxHp = Binding(0)
    val xp = Binding(0)
    val xpToLevel = Binding(0)
    val dnaPoints = Binding(0)
    val gold = Binding(0)
    val floor = Binding(1)
    val room = Binding(1)
    val isDuringBattle = Binding(false)
}
