package com.runt9.namelessAnomalies.service.skillAction

import com.runt9.namelessAnomalies.model.skill.Skill

interface SkillAction<T> {
    fun useSkill(skill: Skill, target: T)
}
