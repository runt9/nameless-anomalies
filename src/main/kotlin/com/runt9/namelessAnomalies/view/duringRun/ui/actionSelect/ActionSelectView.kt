package com.runt9.namelessAnomalies.view.duringRun.ui.actionSelect

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Align
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
                            val text: String
                            val color: Color

                            if (skills.size > i) {
                                val skill = skills[i]
                                val skillName = skill.definition.name
                                if (!skill.isReady) {
                                    text = "$skillName (${skill.remainingCooldown})"
                                    color = Color.DARK_GRAY
                                } else {
                                    text = skillName
                                    color = Color.WHITE
                                }
                            } else {
                                text = ""
                                color = Color.WHITE
                            }

                            label(text) {
                                this.color = color
                                if (skills.size > i) {
                                    onClick { controller.skillSelected(skills[i]) }
                                }
                            }
                        }

                        skillLabel(0).cell(pad = 10f, minWidth = 100f)
                        skillLabel(1).cell(pad = 10f, minWidth = 100f, row = true)
                        skillLabel(2).cell(pad = 10f, minWidth = 100f)
                        skillLabel(3).cell(pad = 10f, minWidth = 100f, row = true)

                        label("Back") {
                            onClick { controller.back() }
                        }.cell(pad = 10f, minWidth = 100f, colspan = 2)
                    } else {
                        label("Skills") {
                            setAlignment(Align.center)
                            onClick { controller.showSkills() }
                        }.cell(grow = true, pad = 10f, minWidth = 100f)

                        label("Rest") {
                            setAlignment(Align.center)
                            onClick { controller.rest() }
                        }.cell(grow = true, pad = 10f, minWidth = 100f, row = true)

                        label("Items") {
                            setAlignment(Align.center)
                            onClick { controller.items() }
                        }.cell(grow = true, colspan = 2, pad = 10f)
                    }
                }
            }
        }
    }
}
