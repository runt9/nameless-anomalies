package com.runt9.namelessAnomalies.service.skillAction

import com.runt9.namelessAnomalies.model.enemy.Enemy
import com.runt9.namelessAnomalies.model.skill.Skill
import com.runt9.namelessAnomalies.model.skill.definition.PrototypeSkillDefinition
import com.runt9.namelessAnomalies.service.RandomizerService
import com.runt9.namelessAnomalies.service.duringRun.RunStateService

class PrototypeSkillAction(private val definition: PrototypeSkillDefinition, private val runStateService: RunStateService, private val randomizerService: RandomizerService) : SkillAction<Enemy> {
    override fun useSkill(skill: Skill, target: Enemy) {
        val anomaly = runStateService.load().anomaly


    }
}
