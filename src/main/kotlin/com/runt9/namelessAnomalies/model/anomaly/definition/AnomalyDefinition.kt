package com.runt9.namelessAnomalies.model.anomaly.definition

import com.runt9.namelessAnomalies.model.TextureDefinition
import com.runt9.namelessAnomalies.model.attribute.AttributeType

enum class AnomalyType { PLAYER, ENEMY }

interface AnomalyDefinition {
    val id: Int
    val name: String
    val texture: TextureDefinition
    val type: AnomalyType
    val attrsPerLevel: Map<AttributeType, Float>
}

fun anomaly(
    id: Int,
    name: String,
    texture: TextureDefinition,
    type: AnomalyType,
    attrsPerLevel: Map<AttributeType, Float> = mapOf()
) = object : AnomalyDefinition {
    override val id = id
    override val name = name
    override val texture = texture
    override val type = type
    override val attrsPerLevel = attrsPerLevel
}
