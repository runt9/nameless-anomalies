package com.runt9.namelessAnomalies.model.anomaly

import com.runt9.namelessAnomalies.model.anomaly.definition.AnomalyDefinition
import com.runt9.namelessAnomalies.model.skill.Skill
import com.runt9.namelessAnomalies.model.skill.SkillTarget
import com.runt9.namelessAnomalies.model.skill.definition.SkillDefinition
import com.runt9.namelessAnomalies.model.skill.definition.prototypeSkill
import kotlinx.serialization.Serializable

@Serializable
class Anomaly(val definition: AnomalyDefinition) : SkillTarget {
    var xp = 0
    var xpToLevel = 10
    var level = 1

    var currentHp = 0
    var maxHp = 0

    val currentSkills = mutableListOf<Skill>(Skill(prototypeSkill))
    val possibleSkills = mutableListOf<SkillDefinition>()
}
