package com.runt9.namelessAnomalies.view.duringRun

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Align
import com.runt9.namelessAnomalies.util.ext.ui.bindUpdatable
import com.runt9.namelessAnomalies.util.ext.ui.rectPixmapTexture
import com.runt9.namelessAnomalies.util.ext.ui.toDrawable
import com.runt9.namelessAnomalies.util.framework.ui.view.ScreenView
import com.runt9.namelessAnomalies.view.duringRun.enemy.EnemyController
import com.runt9.namelessAnomalies.view.duringRun.enemy.enemy
import com.runt9.namelessAnomalies.view.duringRun.ui.actionSelect.actionSelect
import com.runt9.namelessAnomalies.view.duringRun.ui.topBar.topBar
import ktx.actors.onClick
import ktx.scene2d.vis.visImage
import ktx.scene2d.vis.visTable

class DuringRunView(
    override val controller: DuringRunController,
    override val vm: DuringRunViewModel
) : ScreenView() {
    private val enemies = mutableListOf<EnemyController>()

    override fun init() {
        super.init()

        val controller = controller
        val vm = vm

        topBar {
            controller.addChild(this.controller)
            background(rectPixmapTexture(1, 40, Color.SLATE).toDrawable())
        }.cell(growX = true, height = 40f, row = true)

        // Game area
        visTable {
            // Anomaly
            visTable {
                // Anomaly itself
                visTable {
                    bindUpdatable(vm.anomaly) {
                        visImage(vm.anomaly.get()) {
                            setOrigin(Align.center)
                        }
                    }
                }.cell(grow = true, row = true)

                // Action selection
                visTable {
                    actionSelect()
                }.cell(growX = true, pad = 10f, row = true)
            }.cell(grow = true)

            // Enemies
            visTable {
                bindUpdatable(vm.enemies) {
                    clear()
                    vm.enemies.get().forEach { enemy ->
                        // TODO: Some kinda grid-ish layout
                        val enemyView = enemy(enemy) {
                            onClick { controller.enemyClicked(enemy) }
                        }.cell(row = true, pad = 20f)
                        val enemyController = enemyView.controller
                        controller.addChild(enemyController)
                        this@DuringRunView.enemies += enemyController
                    }
                }
            }.cell(grow = true, row = true)
        }.cell(grow = true, row = true)
    }
}
