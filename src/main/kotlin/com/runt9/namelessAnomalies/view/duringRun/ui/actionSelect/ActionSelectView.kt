package com.runt9.namelessAnomalies.view.duringRun.ui.actionSelect

import com.badlogic.gdx.graphics.Color
import com.runt9.namelessAnomalies.util.ext.ui.bindUpdatable
import com.runt9.namelessAnomalies.util.ext.ui.rectPixmapTexture
import com.runt9.namelessAnomalies.util.ext.ui.toDrawable
import com.runt9.namelessAnomalies.util.framework.ui.view.TableView
import ktx.actors.onClick
import ktx.scene2d.label
import ktx.scene2d.vis.visTable

class ActionSelectView(override val controller: ActionSelectController, override val vm: ActionSelectViewModel) : TableView() {
    override fun init() {
        val controller = controller
        val vm = vm

        visTable {
            background(rectPixmapTexture(1, 1, Color.SLATE).toDrawable())

            visTable {
                bindUpdatable(vm.showingSkills) {
                    clear()
                    if (vm.showingSkills.get()) {
                        val skills = vm.skillOptions.get()
                        val skillLabel = { i: Int ->
                            label(if (skills.size > i) skills[i].definition.name else "") {
                                onClick { controller.skillSelected(skills[i]) }
                            }
                        }

                        skillLabel(0).cell(pad = 10f, minWidth = 100f)
                        skillLabel(1).cell(pad = 10f, minWidth = 100f, row = true)
                        skillLabel(2).cell(pad = 10f, minWidth = 100f)
                        skillLabel(3).cell(pad = 10f, minWidth = 100f)
                    } else {
                        label("Skills") {
                            onClick { controller.showSkills() }
                        }.cell(expand = true, pad = 10f)

                        label("Rest") {
                            onClick { controller.rest() }
                        }.cell(expand = true, pad = 10f, row = true)

                        label("Items") {
                            onClick { controller.items() }
                        }.cell(expand = true, colspan = 2, pad = 10f)
                    }
                }
            }
        }
    }
}
