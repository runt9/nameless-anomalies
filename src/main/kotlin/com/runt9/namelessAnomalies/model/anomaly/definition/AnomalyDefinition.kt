package com.runt9.namelessAnomalies.model.anomaly.definition

import com.runt9.namelessAnomalies.model.TextureDefinition

interface AnomalyDefinition {
    val id: Int
    val name: String
    val texture: TextureDefinition
}
