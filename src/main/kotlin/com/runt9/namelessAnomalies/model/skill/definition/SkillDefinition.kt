package com.runt9.namelessAnomalies.model.skill.definition

import com.runt9.namelessAnomalies.model.enemy.Enemy
import com.runt9.namelessAnomalies.model.skill.action.SkillActionDefinition
import com.runt9.namelessAnomalies.service.skillAction.PrototypeSkillAction

enum class SkillTargetType {
    SINGLE, ALL_ENEMIES, SELF
}

interface SkillDefinition {
    val id: Int
    val name: String
    val target: SkillTargetType
    val action: SkillActionDefinition<Any>
}

class PrototypeSkillDefinition : SkillActionDefinition<Enemy>(PrototypeSkillAction::class)

val prototypeSkill = object : SkillDefinition {
    override val id = 1
    override val name = "Prototype"
    override val target = SkillTargetType.SINGLE
    override val action = PrototypeSkillDefinition()
}
