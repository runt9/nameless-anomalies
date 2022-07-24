package com.runt9.namelessAnomalies.view.duringRun.ui.player

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.VisUI
import com.runt9.namelessAnomalies.model.attribute.AttributePriority
import com.runt9.namelessAnomalies.model.attribute.displayName
import com.runt9.namelessAnomalies.model.attribute.getDisplayValue
import com.runt9.namelessAnomalies.model.attribute.priority
import com.runt9.namelessAnomalies.util.ext.loadTexture
import com.runt9.namelessAnomalies.util.ext.ui.bindLabelText
import com.runt9.namelessAnomalies.util.ext.ui.rectPixmapTexture
import com.runt9.namelessAnomalies.util.ext.ui.toDrawable
import com.runt9.namelessAnomalies.util.framework.ui.view.DialogView
import ktx.actors.onChange
import ktx.assets.async.AssetStorage
import ktx.scene2d.KTable
import ktx.scene2d.checkBox
import ktx.scene2d.label
import ktx.scene2d.progressBar
import ktx.scene2d.stack
import ktx.scene2d.textButton
import ktx.scene2d.vis.visImage
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTable
import ktx.style.progressBar

// TODO: Stripe table rows
class PlayerAnomalyDialogView(
    override val controller: PlayerAnomalyDialogController,
    override val vm: PlayerAnomalyDialogViewModel,
    private val assets: AssetStorage
) : DialogView(controller, "Player Anomaly") {
    override val widthScale = 0.75f
    override val heightScale = 0.9f

    override fun KTable.initContentTable() {
        val anomaly = vm.anomaly.get()

        visTable {
            visTable {
                visImage(assets.loadTexture(anomaly.definition.texture)).cell(pad = 10f)

                visTable {
                    val xpBarStyle = VisUI.getSkin().progressBar {
                        background = rectPixmapTexture(2, 2, Color.DARK_GRAY).toDrawable()
                        background.minHeight = 20f
                        background.minWidth = 0f
                        knobBefore = rectPixmapTexture(2, 2, Color.SLATE).toDrawable()
                        knobBefore.minHeight = 20f
                        knobBefore.minWidth = 0f
                    }

                    stack {
                        progressBar {
                            style = xpBarStyle
                            value = anomaly.xp.toFloat() / anomaly.xpToLevel

                            setSize(100f, 20f)
                            setOrigin(Align.center)
                            setRound(false)
                        }

                        label("${anomaly.xp} / ${anomaly.xpToLevel}") {
                            setAlignment(Align.center)
                        }
                    }.cell(width = 100f, height = 20f, row = true)

                    label("") {
                        bindLabelText { "Level ${anomaly.level}" }
                    }.cell(row = true, expand = true, align = Align.center)
                }
                visTable {
                    // TODO: Types
//                    bindUpdatable(vm.classes) {
//                        clear()
//                        vm.classes().forEach { (c, v) ->
//                            label(c.name).cell(expand = true, align = Align.left)
//                            label(v.toString()).cell(row = true, expand = true, align = Align.center)
//                        }
//                    }
                }
            }.cell(grow = true, row = true, spaceBottom = 20f)

            visTable {
                visTable {
                    label("Primary Attributes").cell(row = true, spaceBottom = 10f)
                    visTable {
                        anomaly.attrs.filter { it.key.priority == AttributePriority.PRIMARY }.forEach { (type, attr) ->
                            visLabel(type.displayName).cell(align = Align.left, expandX = true)
                            visLabel(attr.getDisplayValue()).cell(align = Align.center, row = true)
                        }
                    }
                }.cell(grow = true, row = true, spaceBottom = 20f)

                visTable {
                    label("Passives").cell(expandX = true, row = true, align = Align.center, spaceBottom = 10f)

//                    visTable {
//                        bindUpdatable(anomaly.passives) {
//                            clear()
//                            anomaly.passives.forEach {
//                                label("- ${it.name}").cell(expandX = true, row = true)
//                            }
//                        }
//                    }.cell(grow = true, row = true)
                }.cell(grow = true, row = true, pad = 5f)
            }
        }.cell(growX = true, pad = 20f)

        visTable {
            // TODO: Scrollable
            visTable {
                label("Other Attributes").cell(row = true, spaceBottom = 10f)
                visTable {
                    anomaly.attrs.filter { it.key.priority != AttributePriority.PRIMARY }.forEach { (type, attr) ->
                        visLabel(type.displayName).cell(align = Align.left, expandX = true)
                        visLabel(attr.getDisplayValue()).cell(align = Align.center, row = true)
                    }
                }
            }.cell(grow = true, row = true, spaceBottom = 20f)

            visTable {
                // TODO: Scrollable
                label("Skills").cell(row = true, spaceBottom = 10f)
                visTable {
                    anomaly.possibleSkills.forEach { skill ->
                        visLabel(skill.name).cell(align = Align.left, expandX = true)
                        checkBox("") {
                            isChecked = anomaly.activeSkills.map { it.definition }.contains(skill)

                            onChange {
                                controller.updateSkill(skill, isChecked)
                            }
                        }.cell(align = Align.right, row = true, pad = 5f)
                    }
                }
            }
        }.cell(grow = true)
    }

    override fun KTable.initButtons() {
        textButton("Close", "round") { onChange { controller.close() } }.cell(row = true, spaceBottom = 5f)
    }
}
