package com.runt9.namelessAnomalies.view.duringRun.ui.loot.mutation

import com.badlogic.gdx.graphics.Color
import com.kotcrab.vis.ui.VisUI
import com.runt9.namelessAnomalies.util.ext.ui.separator
import com.runt9.namelessAnomalies.util.ext.ui.squarePixmap
import com.runt9.namelessAnomalies.util.framework.ui.view.DialogView
import ktx.actors.onClick
import ktx.scene2d.KTable
import ktx.scene2d.container
import ktx.scene2d.stack
import ktx.scene2d.tooltip
import ktx.scene2d.vis.visLabel

class MutationSelectDialogView(
    override val controller: MutationSelectDialogController,
    override val vm: MutationSelectDialogViewModel
) : DialogView(controller, "Choose Mutation") {
    override val widthScale = 0.25f
    override val heightScale = 0.33f

    override fun KTable.initContentTable() {
        val vm = vm
        val controller = controller

        stack {
            container { squarePixmap(40, Color.LIGHT_GRAY) }
            container { squarePixmap(30, vm.mutation1.type.color) }

            onClick {
                controller.mutationChosen(vm.mutation1, vm.mutation2)
            }

            tooltip {
                it.setInstant(true)
                background(VisUI.getSkin().getDrawable("panel1"))

                visLabel(vm.mutation1.name).cell(growX = true, row = true, pad = 4f)

                separator(2f)

                vm.mutation1.modifiers.forEach { m ->
                    visLabel(m.name).cell(growX = true, row = true, pad = 4f)
                }
            }
        }

        stack {
            container { squarePixmap(40, Color.LIGHT_GRAY) }
            container { squarePixmap(30, vm.mutation2.type.color) }

            onClick {
                controller.mutationChosen(vm.mutation2, vm.mutation1)
            }

            tooltip {
                it.setInstant(true)
                background(VisUI.getSkin().getDrawable("panel1"))

                visLabel(vm.mutation2.name).cell(growX = true, row = true, pad = 4f)

                separator(2f)

                vm.mutation2.modifiers.forEach { m ->
                    visLabel(m.name).cell(growX = true, row = true, pad = 4f)
                }
            }
        }
    }

    override fun KTable.initButtons() {

    }
}
