package com.runt9.namelessAnomalies.model.anomaly.definition

import com.runt9.namelessAnomalies.model.TextureDefinition

enum class AnomalyType { PLAYER, ENEMY }

interface AnomalyDefinition {
    val id: Int
    val name: String
    val texture: TextureDefinition
    val type: AnomalyType
}
