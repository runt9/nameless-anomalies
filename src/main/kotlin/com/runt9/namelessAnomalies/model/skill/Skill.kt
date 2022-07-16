package com.runt9.namelessAnomalies.model.skill

import com.runt9.namelessAnomalies.model.skill.definition.SkillDefinition
import kotlinx.serialization.Serializable

interface SkillTarget

@Serializable
class Skill(val definition: SkillDefinition) {
}
