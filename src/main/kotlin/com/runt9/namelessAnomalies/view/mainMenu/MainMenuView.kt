package com.runt9.namelessAnomalies.view.mainMenu

import com.runt9.namelessAnomalies.util.framework.ui.view.ScreenView
import com.runt9.namelessAnomalies.util.framework.ui.viewModel.ViewModel
import ktx.actors.onClick
import ktx.scene2d.textButton
import ktx.scene2d.vis.visLabel

class MainMenuView(
    override val controller: MainMenuScreenController,
    override val vm: ViewModel
) : ScreenView() {
    override fun init() {
        super.init()

        visLabel("Nameless Anomalies", "title").cell(row = true, spaceBottom = 20f)

        textButton("New Run", "round") {
            onClick { this@MainMenuView.controller.newRun() }
        }.cell(row = true, spaceBottom = 10f)

        textButton("Settings", "round") {
            onClick { this@MainMenuView.controller.showSettings() }
        }.cell(row = true, spaceBottom = 10f)

        textButton("Quit", "round") {
            onClick { this@MainMenuView.controller.exit() }
        }.cell(row = true)
    }
}
