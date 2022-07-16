package com.runt9.namelessAnomalies.model.enemy.definition

import com.runt9.namelessAnomalies.model.TextureDefinition

interface EnemyDefinition {
    val id: Int
    val name: String
    val texture: TextureDefinition
}

val prototypeEnemy = object : EnemyDefinition {
    override val id = 1
    override val name = "Prototype Enemy"
    override val texture = TextureDefinition.RED_ARROW
}
