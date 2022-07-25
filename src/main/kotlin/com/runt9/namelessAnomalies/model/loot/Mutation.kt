package com.runt9.namelessAnomalies.model.loot

import com.runt9.namelessAnomalies.model.attribute.AttributeModifier
import com.runt9.namelessAnomalies.util.ext.displayName
import kotlinx.serialization.Serializable

@Serializable
class Mutation(override val rarity: Rarity, val modifiers: List<AttributeModifier>) : LootItem {
    override val name = "${rarity.displayName()} Mutation"
    override val type = LootItemType.MUTATION
    override val description by lazy { generateDescription() }

    private fun generateDescription(): String {
        val lines = mutableListOf<String>()
        modifiers.forEach { lines += it.name }
        return lines.joinToString("\n")
    }
}
