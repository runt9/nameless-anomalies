package com.runt9.namelessAnomalies.view.duringRun.game

import com.runt9.namelessAnomalies.util.framework.ui.view.TableView
import com.runt9.namelessAnomalies.view.duringRun.GAME_HEIGHT
import com.runt9.namelessAnomalies.view.duringRun.GAME_WIDTH

class DuringRunGameView(
    override val controller: DuringRunGameController,
    override val vm: DuringRunGameViewModel
) : TableView() {
    override fun init() {
        val vm = vm
        val controller = controller

        setSize(GAME_WIDTH, GAME_HEIGHT)
    }
}
