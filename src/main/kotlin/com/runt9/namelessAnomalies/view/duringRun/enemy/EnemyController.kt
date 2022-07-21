package com.runt9.namelessAnomalies.view.duringRun.enemy

import com.runt9.namelessAnomalies.model.anomaly.maxHp
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.ui.controller.Controller
import com.runt9.namelessAnomalies.util.framework.ui.controller.lazyInjectView
import com.runt9.namelessAnomalies.util.framework.ui.uiComponent
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import kotlin.math.roundToInt

@Scene2dDsl
fun <S> KWidget<S>.enemy(enemy: EnemyViewModel, init: EnemyView.(S) -> Unit = {}) = uiComponent<S, EnemyController, EnemyView>({
    this.vm = enemy
}, init)

class EnemyController(private val eventBus: EventBus) : Controller {
    override lateinit var vm: EnemyViewModel
    override val view by lazyInjectView<EnemyView>()

    override fun load() {
        eventBus.registerHandlers(this)
        vm.enemy.onHpChange {
            vm.currentHp(currentHp)
            vm.maxHp(maxHp().roundToInt())
        }
    }

    override fun dispose() {
        eventBus.unregisterHandlers(this)
        super.dispose()
    }
}
