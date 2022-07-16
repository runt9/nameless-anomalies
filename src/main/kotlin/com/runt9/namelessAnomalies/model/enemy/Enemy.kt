package com.runt9.namelessAnomalies.model.enemy

import com.runt9.namelessAnomalies.model.enemy.definition.EnemyDefinition
import com.runt9.namelessAnomalies.model.skill.SkillTarget
import kotlinx.serialization.Serializable

@Serializable
class Enemy(val definition: EnemyDefinition) : SkillTarget {
    var currentHp = 0
    var maxHp = 0
    var isAlive = true

    lateinit var onHpChangeCb: Enemy.() -> Unit

    fun onHpChange(onHpChangeCb: Enemy.() -> Unit) {
        this.onHpChangeCb = onHpChangeCb
    }
}
