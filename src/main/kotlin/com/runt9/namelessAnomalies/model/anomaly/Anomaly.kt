package com.runt9.namelessAnomalies.model.anomaly

import com.runt9.namelessAnomalies.model.anomaly.definition.AnomalyDefinition
import com.runt9.namelessAnomalies.model.attribute.Attribute
import com.runt9.namelessAnomalies.model.attribute.AttributeType
import com.runt9.namelessAnomalies.model.skill.Skill
import com.runt9.namelessAnomalies.model.skill.SkillDefinition
import com.runt9.namelessAnomalies.model.skill.SkillTarget
import com.runt9.namelessAnomalies.model.skill.prototypeSkill
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

const val ANOMALY_MAX_LEVEL = 20

@Serializable
class Anomaly(val definition: AnomalyDefinition, val isPlayer: Boolean) : SkillTarget {
    var xp = 0
    var xpToLevel = 10
    var level = 1

    var currentHp = 0
    var turnDelay = 0

    val attrs = AttributeType.values().associateWith { Attribute(it) }

    val isAlive get() = currentHp > 0

    val currentSkills = mutableListOf<Skill>(Skill(prototypeSkill))
    val possibleSkills = mutableListOf<SkillDefinition>(prototypeSkill)

    fun recalculateAttrs() {
        attrs.values.forEach(Attribute::recalculate)
    }

    fun gainXp(xp: Int): Boolean {
        if (level == ANOMALY_MAX_LEVEL) return false

        this.xp += xp

        if (this.xp >= xpToLevel) {
            level++
            this.xp -= xpToLevel
            xpToLevel = (xpToLevel * 1.5).roundToInt()
            return true
        }

        return false
    }

    fun gainLevel() {
        // TODO: Come back to this in a minute. Probably needs to be in AttributeService. Definition needs attribute growth
        //   And also will need the level-up rewards like DNA points, passive choices, etc
    }
}

val Anomaly.body get() = attrs[AttributeType.BODY]!!
val Anomaly.mind get() = attrs[AttributeType.MIND]!!
val Anomaly.instinct get() = attrs[AttributeType.INSTINCT]!!
val Anomaly.luck get() = attrs[AttributeType.LUCK]!!
val Anomaly.maxHp get() = attrs[AttributeType.MAX_HP]!!
val Anomaly.dodge get() = attrs[AttributeType.DODGE_CHANCE]!!
val Anomaly.damageResist get() = attrs[AttributeType.DAMAGE_RESISTANCE]!!
val Anomaly.baseDamage get() = attrs[AttributeType.BASE_DAMAGE]!!
val Anomaly.critChance get() = attrs[AttributeType.CRIT_CHANCE]!!
val Anomaly.critMulti get() = attrs[AttributeType.CRIT_MULTI]!!
val Anomaly.tdr get() = attrs[AttributeType.TURN_DELAY_REDUCTION]!!
val Anomaly.cdr get() = attrs[AttributeType.COOLDOWN_REDUCTION]!!
