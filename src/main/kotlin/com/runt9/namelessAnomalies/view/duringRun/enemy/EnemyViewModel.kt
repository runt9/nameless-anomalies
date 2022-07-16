package com.runt9.namelessAnomalies.view.duringRun.enemy

import com.runt9.namelessAnomalies.model.enemy.Enemy
import com.runt9.namelessAnomalies.util.framework.ui.viewModel.ViewModel

class EnemyViewModel(val enemy: Enemy) : ViewModel() {
    val texture = enemy.definition.texture
    val currentHp = Binding(1)
    val maxHp = Binding(1)
}
