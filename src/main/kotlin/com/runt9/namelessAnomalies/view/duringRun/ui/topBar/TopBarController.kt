package com.runt9.namelessAnomalies.view.duringRun.ui.topBar

import com.runt9.namelessAnomalies.model.anomaly.maxHp
import com.runt9.namelessAnomalies.model.event.AttributesUpdated
import com.runt9.namelessAnomalies.model.event.HpChanged
import com.runt9.namelessAnomalies.model.event.RunStateUpdated
import com.runt9.namelessAnomalies.model.event.enqueueShowDialog
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.event.HandlesEvent
import com.runt9.namelessAnomalies.util.framework.ui.controller.Controller
import com.runt9.namelessAnomalies.util.framework.ui.uiComponent
import com.runt9.namelessAnomalies.view.duringRun.ui.map.MapDialogController
import com.runt9.namelessAnomalies.view.duringRun.ui.menu.MenuDialogController
import ktx.async.onRenderingThread
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import kotlin.math.roundToInt

@Scene2dDsl
fun <S> KWidget<S>.topBar(init: TopBarView.(S) -> Unit = {}) = uiComponent<S, TopBarController, TopBarView>(init = init)

class TopBarController(private val eventBus: EventBus) : Controller {
    override val vm = TopBarViewModel()
    override val view = TopBarView(this, vm)

    @HandlesEvent
    fun attrsUpdated(event: AttributesUpdated) {
        if (!event.anomaly.isPlayer) return

        event.anomaly.apply {
            vm.hp(currentHp)
            vm.maxHp(maxHp().roundToInt())
        }
    }

    @HandlesEvent
    fun hpChanged(event: HpChanged) {
        if (!event.self.isPlayer) return

        event.self.apply {
            vm.hp(currentHp)
            vm.maxHp(maxHp().roundToInt())
        }
    }

    @HandlesEvent
    suspend fun runStateHandler(event: RunStateUpdated) = onRenderingThread {
        val newState = event.newState
        vm.apply {
            hp(newState.anomaly.currentHp)
            maxHp(newState.anomaly.maxHp().roundToInt())
            xp(newState.anomaly.xp)
            xpToLevel(newState.anomaly.xpToLevel)
            gold(newState.gold)
            dnaPoints(newState.dnaPoints)
            floor(newState.floor)
        }
    }

//    @HandlesEvent(NewBattleEvent::class) suspend fun newBattle() = onRenderingThread { vm.isDuringBattle(false) }
//    @HandlesEvent(BattleStartedEvent::class) suspend fun battleStart() = onRenderingThread { vm.isDuringBattle(true) }
//    @HandlesEvent(BattleCompletedEvent::class) suspend fun battleComplete() = onRenderingThread { vm.isDuringBattle(false) }

    override fun load() {
        eventBus.registerHandlers(this)
    }

    fun mapButtonClicked() = eventBus.enqueueShowDialog<MapDialogController>()
    fun anomalyButtonClicked() = Unit
    fun bossButtonClicked() = Unit
    fun menuButtonClicked() = eventBus.enqueueShowDialog<MenuDialogController>()

    override fun dispose() {
        eventBus.unregisterHandlers(this)
        super.dispose()
    }
}
