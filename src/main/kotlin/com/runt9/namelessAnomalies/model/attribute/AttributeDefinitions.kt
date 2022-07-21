package com.runt9.namelessAnomalies.model.attribute

import com.runt9.namelessAnomalies.util.ext.displayDecimal
import com.runt9.namelessAnomalies.util.ext.displayInt
import com.runt9.namelessAnomalies.util.ext.displayMultiplier
import com.runt9.namelessAnomalies.util.ext.displayPercent

// Primary
val body = primaryAttr("Body", "Body")
val mind = primaryAttr("Mind", "Mind")
val instinct = primaryAttr("Instinct", "Instinct")
val luck = primaryAttr("Luck", "Luck")

// Secondary
val maxHp = attributeDef(AttributePriority.SECONDARY, "HP", "Max HP", AttributeRandomRange(10f..25f, 5f..10f), AttributeValueClamp(min = 1f), 100f, Float::displayInt)
val damageResist = attributeDef(AttributePriority.SECONDARY, "DR", "Damage Resistance", AttributeRandomRange(0.02f..0.05f, 2f..5f), AttributeValueClamp(min = 0.5f), 1.05f, Float::displayDecimal)
val dodge = attributeDef(AttributePriority.SECONDARY, "Dodge", "Dodge Chance", AttributeRandomRange(0.005f..0.01f, 20f..50f), AttributeValueClamp(min = 0f, max = 1f), 0.05f, Float::displayPercent)
val tdr = attributeDef(AttributePriority.SECONDARY, "TDR", "Turn Delay Reduction", AttributeRandomRange(0.02f..0.05f, 2f..5f), AttributeValueClamp(min = 0.5f), 1.00f, Float::displayDecimal)
val damage = attributeDef(AttributePriority.SECONDARY, "Dmg", "Base Damage", AttributeRandomRange(5f..10f, 10f..20f), AttributeValueClamp(min = 1f), 20f, Float::displayInt)
val critChance = attributeDef(AttributePriority.SECONDARY, "Crit%", "Crit Chance", AttributeRandomRange(0.005f..0.01f, 20f..50f), AttributeValueClamp(min = 0f, max = 1f), 0.05f, Float::displayPercent)
val critMulti = attributeDef(AttributePriority.SECONDARY, "CritDmg", "Crit Multiplier", AttributeRandomRange(0.1f..0.25f, 10f..30f), AttributeValueClamp(min = 1f), 1.5f, Float::displayMultiplier)
val cdr = attributeDef(AttributePriority.SECONDARY, "CDR", "Cooldown Reduction", AttributeRandomRange(0.02f..0.05f, 2f..5f), AttributeValueClamp(min = 0.5f), 1.00f, Float::displayDecimal)
