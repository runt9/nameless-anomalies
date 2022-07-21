package com.runt9.namelessAnomalies.service

import com.runt9.namelessAnomalies.service.duringRun.RunService
import com.runt9.namelessAnomalies.service.duringRun.RunServiceRegistry
import com.runt9.namelessAnomalies.service.duringRun.RunStateService
import com.runt9.namelessAnomalies.util.ext.random
import com.runt9.namelessAnomalies.util.framework.event.EventBus
import kotlin.random.Random

class RandomizerService(private val runStateService: RunStateService, eventBus: EventBus, registry: RunServiceRegistry) : RunService(eventBus, registry) {
    lateinit var rng: Random

    override fun startInternal() {
        val state = runStateService.load()
        rng = Random(state.seed.hashCode())
    }

    fun <T> randomize(action: (Random) -> T) = action(rng)

    fun <T : Comparable<T>> randomize(lucky: Boolean = false, action: (Random) -> T): T {
        val first = action(rng)
        if (lucky) {
            val second = action(rng)
            return maxOf(first, second)
        }

        return first
    }

    fun percentChance(percentChance: Float, lucky: Boolean = false) = randomize(lucky) { it.nextFloat() } <= percentChance
    fun coinFlip(lucky: Boolean = false) = randomize(lucky) { rng.nextBoolean() }

    fun range(range: ClosedFloatingPointRange<Float>, lucky: Boolean = false) = randomize(lucky) {
        range.random(it)
    }
}
