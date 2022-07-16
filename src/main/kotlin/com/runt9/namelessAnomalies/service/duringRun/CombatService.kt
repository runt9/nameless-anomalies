package com.runt9.namelessAnomalies.service.duringRun

import com.runt9.namelessAnomalies.model.enemy.Enemy
import com.runt9.namelessAnomalies.model.skill.Skill
import com.runt9.namelessAnomalies.model.skill.action.SkillActionDefinition
import com.runt9.namelessAnomalies.util.ext.dynamicInject
import com.runt9.namelessAnomalies.util.ext.dynamicInjectCheckIsSubclassOf
import com.runt9.namelessAnomalies.util.ext.naLogger
import com.runt9.namelessAnomalies.util.framework.event.EventBus

class CombatService(eventBus: EventBus, registry: RunServiceRegistry, private val runStateService: RunStateService) : RunService(eventBus, registry) {
    private val logger = naLogger()

    fun useSkillOnEnemy(skill: Skill, enemy: Enemy) {
        val action = dynamicInject(
            skill.definition.action.actionClass,
            dynamicInjectCheckIsSubclassOf(SkillActionDefinition::class.java) to skill.definition
        )

        action.useSkill(skill, enemy)
    }
}
