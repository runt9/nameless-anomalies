package com.runt9.namelessAnomalies.service.duringRun

import com.runt9.namelessAnomalies.model.RunState
import com.runt9.namelessAnomalies.model.event.RunStateUpdated
import com.runt9.namelessAnomalies.model.interceptor.InterceptableContext
import com.runt9.namelessAnomalies.model.interceptor.InterceptorHook
import com.runt9.namelessAnomalies.util.ext.naLogger
import com.runt9.namelessAnomalies.util.framework.event.EventBus

class RunStateService(private val eventBus: EventBus, registry: RunServiceRegistry) : RunService(eventBus, registry) {
    private val logger = naLogger()
    private lateinit var runState: RunState

    // TODO: This should probably jump into the service thread to load
    fun load() = runState.copy()

    fun save(runState: RunState) {
        if (!this@RunStateService::runState.isInitialized || runState != this@RunStateService.runState) {
            logger.debug { "Saving run state" }
            this@RunStateService.runState = runState
            eventBus.enqueueEventSync(RunStateUpdated(runState.copy()))
            // TODO: This should also flush the current state to disk
        }
    }

    fun update(update: RunState.() -> Unit) = launchOnServiceThread {
        load().apply {
            update()
            save(this)
        }
    }

    fun intercept(hook: InterceptorHook) = load().intercept(hook)
    fun intercept(hook: InterceptorHook, context: InterceptableContext) = load().intercept(hook, context)
}
