package com.runt9.namelessAnomalies.view.duringRun.ui.loot

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.runt9.namelessAnomalies.util.ext.ui.bindUpdatable
import com.runt9.namelessAnomalies.util.framework.ui.view.DialogView
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.actors.then
import ktx.scene2d.KTable
import ktx.scene2d.textButton

class LootDialogView(
    override val controller: LootDialogController,
    override val vm: LootDialogViewModel
) : DialogView(controller, "Rewards") {
    override val widthScale: Float = 0.25f
    override val heightScale: Float = 0.75f

    override fun KTable.initContentTable() {
        val buttonHandler: TextButton.() -> Unit = {
            this += Actions.fadeOut(0.1f) then Actions.removeActor()
//            val cell = getCell(this)
//            cell.height(0f).space(0f)
        }

        textButton("${vm.xp()} XP") {
            onClick {
                controller.gainXp()
                buttonHandler()
            }
        }.cell(growX = true, row = true)

        textButton("${vm.cells()} Cells") {
            onClick {
                controller.gainCells()
                buttonHandler()
            }
        }.cell(growX = true, row = true)

        bindUpdatable(vm.levelUp) {
            if (vm.levelUp.get()) {
                textButton("Level up!") {
                    onClick {
                        controller.levelUp()
                        buttonHandler()
                    }
                }.cell(growX = true, row = true)
            }
        }
    }

    override fun KTable.initButtons() {
        textButton("Done", "round") { onChange { controller.done() } }.cell(row = true, spaceBottom = 5f)
    }
}
