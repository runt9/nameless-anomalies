package com.runt9.namelessAnomalies.view.duringRun.ui.topBar

import com.badlogic.gdx.utils.Align
import com.runt9.namelessAnomalies.util.ext.ui.bindButtonDisabled
import com.runt9.namelessAnomalies.util.ext.ui.bindLabelText
import com.runt9.namelessAnomalies.util.framework.ui.view.TableView
import ktx.actors.onChange
import ktx.scene2d.textButton
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTable

class TopBarView(override val controller: TopBarController, override val vm: TopBarViewModel) : TableView() {
    override fun init() {
        val vm = vm
        val controller = controller

        visTable {
            visLabel("") { bindLabelText { "Cells: ${vm.cells()}" } }.cell(expand = true)
            visLabel("") { bindLabelText { "DNA: ${vm.dnaPoints()}" } }.cell(expand = true)
            visLabel("") { bindLabelText { "HP: ${vm.hp()} / ${vm.maxHp()}" } }.cell(expand = true)
            visLabel("") { bindLabelText { "XP: ${vm.xp()} / ${vm.xpToLevel()}" } }.cell(expand = true)
        }.cell(expand = true, align = Align.left, padLeft = 5f)

        visTable {
            visLabel("") { bindLabelText { "Floor: ${vm.floor()}" } }.cell(expand = true)

            textButton("Map") {
                bindButtonDisabled(vm.isDuringBattle, true)
                onChange { controller.mapButtonClicked() }
            }.cell(expand = true)

            textButton("Anomaly") {
                bindButtonDisabled(vm.isDuringBattle, true)
                onChange { controller.anomalyButtonClicked() }
            }.cell(expand = true)

            textButton("Boss") {
                bindButtonDisabled(vm.isDuringBattle, true)
                onChange { controller.bossButtonClicked() }
            }.cell(expand = true)

            textButton("Menu") {
                onChange { controller.menuButtonClicked() }
            }
        }.cell(expand = true, align = Align.right, padRight = 5f)
    }
}
