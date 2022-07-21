package com.runt9.namelessAnomalies.model.anomaly.definition

import com.runt9.namelessAnomalies.model.TextureDefinition

var prototypeAnomaly = object : AnomalyDefinition {
    override val id = 1
    override val name = "Prototype"
    override val texture = TextureDefinition.HERO_ARROW
    override val type = AnomalyType.PLAYER
}

val prototypeEnemy = object : AnomalyDefinition {
    override val id = 2
    override val name = "Prototype Enemy"
    override val texture = TextureDefinition.RED_ARROW
    override val type = AnomalyType.ENEMY
}
