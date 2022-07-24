package com.runt9.namelessAnomalies.model.anomaly.definition

import com.runt9.namelessAnomalies.model.TextureDefinition
import com.runt9.namelessAnomalies.model.attribute.AttributeType.BODY
import com.runt9.namelessAnomalies.model.attribute.AttributeType.INSTINCT
import com.runt9.namelessAnomalies.model.attribute.AttributeType.LUCK
import com.runt9.namelessAnomalies.model.attribute.AttributeType.MIND

var prototypeAnomaly = anomaly(1, "Prototype", TextureDefinition.HERO_ARROW, AnomalyType.PLAYER, mapOf(BODY to 2f, MIND to 1f, INSTINCT to 1f, LUCK to 2f))
var prototypeEnemy = anomaly(2, "Prototype Enemy", TextureDefinition.RED_ARROW, AnomalyType.ENEMY)
