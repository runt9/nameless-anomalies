package com.runt9.namelessAnomalies.service.duringRun

import com.runt9.namelessAnomalies.model.attribute.AttributeModifier
import com.runt9.namelessAnomalies.model.attribute.AttributeType
import com.runt9.namelessAnomalies.model.loot.Mutation
import com.runt9.namelessAnomalies.model.loot.Rarity
import com.runt9.namelessAnomalies.service.RandomizerService
import com.runt9.namelessAnomalies.util.ext.generateWeightedList
import com.runt9.namelessAnomalies.util.framework.event.EventBus

// TODO: Apply interceptor for lucky rarity/attribute rolls
class LootService(
    eventBus: EventBus,
    registry: RunServiceRegistry,
    private val randomizer: RandomizerService,
    private val runStateService: RunStateService
) : RunService(eventBus, registry) {
    private val rarityWeights = mapOf(
        Rarity.COMMON to 50,
        Rarity.UNCOMMON to 30,
        Rarity.RARE to 15,
        Rarity.LEGENDARY to 5
    )

    private val rarityTable = generateWeightedList(rarityWeights)

    fun generateBattleLoot(): BattleLoot {
        if (randomizer.percentChance(0.25f)) {
            // TODO: Generate consumable
        }

        val state = runStateService.load()

        // TODO: Determine XP/cells algorithm, gonna be similar to scaling enemy difficulty over the length of a floor
        //   Maybe the further from the "entrance" the more cells/xp and the harder the enemies?
        //   Or maybe track the number of rooms visited and scale up based off of that?
        //   For now it's just based on the floor and is static

        val xp = state.floor * 10
        val cells = state.floor * (randomizer.rng.nextInt(15, 25))
        // TODO: Generate two mutations

        return BattleLoot(xp, cells, generateMutation(), generateMutation())
    }

    private fun generateRarity() = randomizer.randomize { rarityTable.random(it) }

    private fun generateMutation(): Mutation {
        val rarity = generateRarity()
        val count = rarity.numMutationAttrs

        val generatedSoFar = mutableListOf<AttributeType>()
        val modifiers = mutableListOf<AttributeModifier>()

        repeat(count) {
            val type = AttributeType.values().filter { !generatedSoFar.contains(it) }.random(randomizer.rng)

            modifiers += randomizer.randomAttributeModifier(false, type)
            generatedSoFar += type
        }

        return Mutation(rarity, modifiers)
    }

    private val Rarity.numMutationAttrs: Int get() = when (this) {
        Rarity.COMMON -> 2
        Rarity.UNCOMMON -> 3
        Rarity.RARE, Rarity.LEGENDARY -> 4
    }

}

data class BattleLoot(val xp: Int, val cells: Int, val mutation1: Mutation, val mutation2: Mutation)
