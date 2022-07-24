package com.runt9.namelessAnomalies.view.duringRun.enemy

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.VisUI
import com.runt9.namelessAnomalies.util.ext.loadTexture
import com.runt9.namelessAnomalies.util.ext.ui.bindLabelText
import com.runt9.namelessAnomalies.util.ext.ui.bindUpdatable
import com.runt9.namelessAnomalies.util.ext.ui.rectPixmapTexture
import com.runt9.namelessAnomalies.util.ext.ui.toDrawable
import com.runt9.namelessAnomalies.util.framework.ui.view.TableView
import ktx.assets.async.AssetStorage
import ktx.scene2d.label
import ktx.scene2d.progressBar
import ktx.scene2d.stack
import ktx.scene2d.vis.visImage
import ktx.scene2d.vis.visTable
import ktx.style.progressBar

class EnemyView(
    override val controller: EnemyController,
    override val vm: EnemyViewModel,
    private val assets: AssetStorage
) : TableView() {
    override fun init() {
        val vm = vm

        setOrigin(Align.center)

        visTable {
            stack {
                progressBar {
                    style = VisUI.getSkin().progressBar {
                        background = rectPixmapTexture(1, 1, Color.DARK_GRAY).toDrawable()
                        background.minHeight = 20f
                        background.minWidth = 0f
                        knobBefore = rectPixmapTexture(1, 1, Color(0.5f, 0f, 0f, 1f)).toDrawable()
                        knobBefore.minHeight = 20f
                        knobBefore.minWidth = 0f
                    }

                    bindUpdatable(vm.currentHp) {
                        if (!vm.enemy.isAlive) return@bindUpdatable
                        value = vm.currentHp.get().toFloat() / vm.maxHp.get().toFloat()
                    }

                    setAnimateDuration(0.15f)
                    setSize(100f, 20f)
                    setOrigin(Align.center)
                    setRound(false)
                }

                label("") {
                    bindLabelText { "${vm.currentHp()} / ${vm.maxHp()}" }
                    setAlignment(Align.center)
                }
            }.cell(height = 20f, width = 100f, row = true)
        }.cell(row = true, padBottom = 10f)

        visImage(assets.loadTexture(vm.texture)) {
            setOrigin(Align.center)
        }.cell()
    }
}
