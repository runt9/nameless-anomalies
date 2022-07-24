package com.runt9.namelessAnomalies.view.duringRun.ui.actionSelect

import com.runt9.namelessAnomalies.model.event.SkillSelected
import com.runt9.namelessAnomalies.model.event.TurnComplete
import com.runt9.namelessAnomalies.model.skill.Skill
import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.service.duringRun.SkillService
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.ui.controller.Controller
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView
import com.runt9.namelessAnomalies.util.framework.ui.uiComponent
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl

@Scene2dDsl
fun <S> KWidget<S>.actionSelect(init: ActionSelectView.(S) -> Unit = {}) = uiComponent<S, ActionSelectController, ActionSelectView>(init = init)

class ActionSelectController(private val runStateService: RunStateService, private val eventBus: EventBus, private val skillService: SkillService) : Controller {
    override val vm = ActionSelectViewModel()
    override val view = injectView<ActionSelectView>()

    fun showSkills() {
        if (!vm.canInteract.get()) return

        runStateService.load().apply {
            vm.skillOptions(anomaly.activeSkills)
        }

        vm.showingSkills(true)
    }

    fun rest() {
        if (!vm.canInteract.get()) return

        skillService.rest(runStateService.load().anomaly)
        eventBus.enqueueEventSync(TurnComplete())
    }

    fun items() {
        if (!vm.canInteract.get()) return
    }

    fun skillSelected(skill: Skill) {
        if (!vm.canInteract.get()) return
        if (!skill.isReady) return

        vm.canInteract(false)
        eventBus.enqueueEventSync(SkillSelected(skill))
    }

    fun back() {
        vm.showingSkills(false)
    }
}
