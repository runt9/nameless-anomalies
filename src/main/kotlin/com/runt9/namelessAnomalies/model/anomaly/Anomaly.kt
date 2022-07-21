package com.runt9.namelessAnomalies.model.anomaly

import com.runt9.namelessAnomalies.model.anomaly.definition.AnomalyDefinition
import com.runt9.namelessAnomalies.model.attribute.Attribute
import com.runt9.namelessAnomalies.model.attribute.AttributeType
import com.runt9.namelessAnomalies.model.skill.Skill
import com.runt9.namelessAnomalies.model.skill.SkillTarget
import com.runt9.namelessAnomalies.model.skill.prototypeSkill
import kotlinx.serialization.Serializable

@Serializable
class Anomaly(val definition: AnomalyDefinition) : SkillTarget {
    var xp = 0
    var xpToLevel = 10
    var level = 1

    var currentHp = 0
    val attrs = AttributeType.values().associateWith { Attribute(it) }

    var isAlive = true

    lateinit var onHpChangeCb: Anomaly.() -> Unit

    fun onHpChange(onHpChangeCb: Anomaly.() -> Unit) {
        this.onHpChangeCb = onHpChangeCb
    }

    val currentSkills = mutableListOf<Skill>(prototypeSkill)
    val possibleSkills = mutableListOf<Skill>(prototypeSkill)
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
