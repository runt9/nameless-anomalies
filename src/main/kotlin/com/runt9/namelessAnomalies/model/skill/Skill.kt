package com.runt9.namelessAnomalies.model.skill

import com.runt9.namelessAnomalies.model.interceptor.InterceptableContext
import com.runt9.namelessAnomalies.model.interceptor.Interceptor
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook
import kotlinx.serialization.Serializable

interface SkillTarget

enum class SkillTargetType {
    SINGLE, ALL_ENEMIES, SELF
}

interface SkillDefinition {
    val id: Int
    val name: String
    val target: SkillTargetType
    val turnDelay: Int
    val cooldown: Int
    val interceptors: MutableMap<InterceptorHook, MutableList<Interceptor<InterceptableContext>>>
}

fun skill(
    id: Int,
    name: String,
    target: SkillTargetType,
    turnDelay: Int,
    cooldown: Int,
    interceptors: MutableMap<InterceptorHook, MutableList<Interceptor<InterceptableContext>>> = mutableMapOf(),
) = object : SkillDefinition {
    override val id = id
    override val name = name
    override val target = target
    override val turnDelay = turnDelay
    override val cooldown = cooldown
    override val interceptors = interceptors
}

val prototypeSkill = skill(1, "Prototype", SkillTargetType.SINGLE, 100, 3)

@Serializable
class Skill(val definition: SkillDefinition) {
    var remainingCooldown = 0
    val isReady get() = remainingCooldown == 0

    fun tickDownCooldown() {
        remainingCooldown = (remainingCooldown - 1).coerceAtLeast(0)
    }
}
