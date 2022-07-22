package com.runt9.namelessAnomalies.view.duringRun.ui.actionSelect

import com.runt9.namelessAnomalies.model.event.SkillSelected
import com.runt9.namelessAnomalies.model.skill.Skill
import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import com.runt9.namelessAnomalies.util.framework.ui.controller.Controller
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView
import com.runt9.namelessAnomalies.util.framework.ui.uiComponent
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl

@Scene2dDsl
fun <S> KWidget<S>.actionSelect(init: ActionSelectView.(S) -> Unit = {}) = uiComponent<S, ActionSelectController, ActionSelectView>(init = init)

class ActionSelectController(private val runStateService: RunStateService, private val eventBus: EventBus) : Controller {
    override val vm = ActionSelectViewModel()
    override val view = injectView<ActionSelectView>()

    fun showSkills() {
        if (!vm.canInteract.get()) return

        runStateService.load().apply {
            vm.skillOptions(anomaly.currentSkills)
        }

        vm.showingSkills(true)
    }

    fun rest() {
        if (!vm.canInteract.get()) return
    }

    fun items() {
        if (!vm.canInteract.get()) return
    }

    fun skillSelected(skill: Skill) {
        if (!vm.canInteract.get()) return
        if (skill.remainingCooldown > 0) return

        vm.canInteract(false)
        eventBus.enqueueEventSync(SkillSelected(skill))
    }
}
