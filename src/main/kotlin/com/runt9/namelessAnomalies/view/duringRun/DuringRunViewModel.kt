package com.runt9.namelessAnomalies.view.duringRun

import com.badlogic.gdx.graphics.Color
import com.runt9.namelessAnomalies.util.ext.ui.rectPixmapTexture
import com.runt9.namelessAnomalies.util.framework.ui.viewModel.ViewModel
import com.runt9.namelessAnomalies.view.duringRun.enemy.EnemyViewModel

class DuringRunViewModel : ViewModel() {
    // TODO: Probably need a better way to have an "empty" texture
    val anomaly = Binding(rectPixmapTexture(1, 1, Color.SLATE))
    val enemies = ListBinding(listOf<EnemyViewModel>())
    val isPlayersTurn = Binding(false)
}
