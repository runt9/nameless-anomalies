package com.runt9.namelessAnomalies.view.duringRun.ui.player

import com.runt9.namelessAnomalies.model.skill.Skill
import com.runt9.namelessAnomalies.model.skill.SkillDefinition
import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.util.ext.naLogger
import com.runt9.namelessAnomalies.util.framework.ui.controller.DialogController
import com.runt9.namelessAnomalies.util.framework.ui.controller.injectView

class PlayerAnomalyDialogController(private val runStateService: RunStateService) : DialogController() {
    private val logger = naLogger()

    override val vm = PlayerAnomalyDialogViewModel()
    override val view = injectView<PlayerAnomalyDialogView>()

    override fun load() {
        runStateService.load().apply {
            vm.anomaly(anomaly)
        }
    }

    fun close() {
        val anomaly = vm.anomaly.get()

        if (anomaly.activeSkills.size == 0) {
            logger.error { "Cannot close with 0 active skills" }
            return
        } else if (anomaly.activeSkills.size > 5) {
            logger.error { "Cannot close with more than 5 active skills" }
            return
        }

        hide()
    }

    fun updateSkill(skill: SkillDefinition, checked: Boolean) {
        val anomaly = vm.anomaly.get()

        if (checked) {
            anomaly.activeSkills += Skill(skill)
        } else {
            anomaly.activeSkills.removeIf { it.definition == skill }
        }
    }
}
