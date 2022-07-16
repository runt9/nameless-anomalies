package com.runt9.namelessAnomalies.util.framework.event

interface Event {
    val name: String get() = this::class.simpleName ?: "Event"
}
