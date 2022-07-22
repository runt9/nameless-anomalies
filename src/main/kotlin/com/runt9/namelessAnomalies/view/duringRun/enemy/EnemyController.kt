package com.runt9.namelessAnomalies.view.duringRun.enemy

import com.runt9.namelessAnomalies.model.anomaly.maxHp
import com.runt9.namelessAnomalies.model.event.AttributesUpdated
import com.runt9.namelessAnomalies.model.event.HpChanged
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.event.HandlesEvent
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

    @HandlesEvent
    fun hpChanged(event: HpChanged) {
        if (event.self != vm.enemy) return
        event.self.apply {
            vm.currentHp(currentHp)
            vm.maxHp(maxHp().roundToInt())
        }
    }

    @HandlesEvent
    fun attrsUpdated(event: AttributesUpdated) {
        if (event.anomaly != vm.enemy) return
        event.anomaly.apply {
            vm.currentHp(currentHp)
            vm.maxHp(maxHp().roundToInt())
        }
    }

    override fun load() {
        eventBus.registerHandlers(this)
    }

    override fun dispose() {
        eventBus.unregisterHandlers(this)
        super.dispose()
    }
}
