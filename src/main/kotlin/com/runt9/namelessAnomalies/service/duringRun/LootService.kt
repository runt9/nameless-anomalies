package com.runt9.namelessAnomalies.service.duringRun

import com.runt9.namelessAnomalies.service.RandomizerService
import com.runt9.namelessAnomalies.util.framework.event.EventBus

class LootService(
    eventBus: EventBus,
    registry: RunServiceRegistry,
    private val randomizer: RandomizerService,
    private val runStateService: RunStateService
) : RunService(eventBus, registry) {
    fun generateBattleLoot(): BattleLoot {
        if (randomizer.percentChance(0.25f)) {
            // TODO: Generate consumable
        }

        val state = runStateService.load()

        // TODO: Determine XP/cells algorithm, gonna be similar to scaling enemy difficulty over the length of a floor
        //   Maybe the further from the "entrance" the more cells/xp and the harder the enemies?
        //   Or maybe track the number of rooms visited and scale up based off of that?
        //   For now it's just based on the floor and is static

        // TODO: Generate XP
        val xp = state.floor * 10
        // TODO: Generate cells
        val cells = state.floor * (randomizer.rng.nextInt(15, 25))
        // TODO: Generate two mutations

        return BattleLoot(xp, cells)
    }
}

data class BattleLoot(val xp: Int, val cells: Int)
