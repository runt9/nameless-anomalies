package com.runt9.namelessAnomalies.model.interceptor

import com.runt9.namelessAnomalies.model.anomaly.Anomaly

enum class InterceptorHook {
    // Skill Interceptors
    BEFORE_SKILL_USED,
    BEFORE_HIT_CHECK,
    ON_HIT,
    ON_DODGE,
    BEFORE_CRIT_CHECK,
    ON_CRIT,
    ON_NON_CRIT,
    BEFORE_DAMAGE_CHECK,
    ON_DAMAGE_DEALT,
    BEFORE_TURN_DELAY_CALC,
    BEFORE_COOLDOWN_CALC,
    AFTER_SKILL_USED,

    // Other Interceptors
    HP_CHANGED
}

interface Interceptor<T : InterceptableContext> {
    val hook: InterceptorHook

    fun intercept(context: T)
    fun canIntercept(context: InterceptableContext): Boolean
}

interface InterceptableContext {
    val interceptors: MutableMap<InterceptorHook, MutableList<Interceptor<InterceptableContext>>>

    fun intercept(hook: InterceptorHook) {
        interceptors[hook]?.filter { it.canIntercept(this) }?.forEach { it.intercept(this) }
    }
}

abstract class InterceptableAdapter : InterceptableContext {
    override val interceptors = mutableMapOf<InterceptorHook, MutableList<Interceptor<InterceptableContext>>>()
}

inline fun <reified T : InterceptableContext> intercept(hook: InterceptorHook, crossinline intercept: (context: T) -> Unit) = object : Interceptor<T> {
    override val hook = hook
    override fun intercept(context: T) = intercept(context)
    override fun canIntercept(context: InterceptableContext) = context::class == T::class
}

class HpChangedContext(val self: Anomaly, hpChange: Float) : InterceptableAdapter()
