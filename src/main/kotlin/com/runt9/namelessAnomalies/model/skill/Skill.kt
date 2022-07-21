package com.runt9.namelessAnomalies.model.skill

import com.runt9.namelessAnomalies.model.interceptor.InterceptableContext
import com.runt9.namelessAnomalies.model.interceptor.Interceptor
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook

interface SkillTarget

enum class SkillTargetType {
    SINGLE, ALL_ENEMIES, SELF
}

interface Skill {
    val id: Int
    val name: String
    val target: SkillTargetType
    val interceptors: MutableMap<InterceptorHook, MutableList<Interceptor<InterceptableContext>>>
}

val prototypeSkill = object : Skill {
    override val id = 1
    override val name = "Prototype"
    override val target = SkillTargetType.SINGLE
    override val interceptors = mutableMapOf<InterceptorHook, MutableList<Interceptor<InterceptableContext>>>()
}
