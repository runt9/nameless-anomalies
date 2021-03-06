package com.runt9.namelessAnomalies.view.duringRun.ui.menu

import com.runt9.namelessAnomalies.util.ext.ui.bindLabelText
import com.runt9.namelessAnomalies.util.framework.ui.view.DialogView
import ktx.actors.onChange
import ktx.scene2d.KTable
import ktx.scene2d.textButton
import ktx.scene2d.vis.visLabel

class MenuDialogView(
    override val controller: MenuDialogController,
    override val vm: MenuDialogViewModel
) : DialogView(controller, "Menu") {
    override val widthScale: Float = 0.33f
    override val heightScale: Float = 0.5f

    override fun KTable.initContentTable() {
        val vm = this@MenuDialogView.vm
        val controller = this@MenuDialogView.controller

        visLabel("") { bindLabelText { "Seed: ${vm.runSeed()}" } }.cell(row = true, spaceBottom = 10f)
        textButton("Resume", "round") { onChange { controller.resume() } }.cell(row = true, spaceBottom = 5f)
        textButton("Settings", "round") { onChange { controller.settings() } }.cell(row = true, spaceBottom = 5f)
        textButton("Exit to Main Menu", "round") { onChange { controller.mainMenu() } }.cell(row = true, spaceBottom = 5f)
        textButton("Exit Game", "round") { onChange { controller.exit() } }
    }

    override fun KTable.initButtons() = Unit
}
