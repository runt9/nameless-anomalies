package com.runt9.namelessAnomalies.model.attribute

enum class AttributeType(val definition: AttributeDefinition) {
    // Primary
    BODY(body),
    MIND(mind),
    INSTINCT(instinct),
    LUCK(luck),

    // Secondary
    MAX_HP(maxHp),
    DAMAGE_RESISTANCE(damageResist),
    DODGE_CHANCE(dodge),
    TURN_DELAY_REDUCTION(tdr),
    BASE_DAMAGE(damage),
    CRIT_CHANCE(critChance),
    CRIT_MULTI(critMulti),
    COOLDOWN_REDUCTION(cdr)
}
