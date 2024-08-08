package xyz.chlamydomonos.hyphacraft.loot

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.ItemLoader
import xyz.chlamydomonos.hyphacraft.loaders.LootLoader
import java.util.function.Consumer

class GrandisporiaWitheredCapLoot(
    weight: Int,
    quality: Int,
    conditions: MutableList<LootItemCondition>,
    functions: MutableList<LootItemFunction>
) : LootPoolSingletonContainer(weight, quality, conditions, functions) {
    companion object {
        val CODEC = RecordCodecBuilder.mapCodec { singletonFields(it).apply(it, ::GrandisporiaWitheredCapLoot) }

        fun builder() = simpleBuilder { w, q, c, f -> GrandisporiaWitheredCapLoot(w, q, c, f) }
    }
    override fun getType() = LootLoader.GRANDISPORIA_WITHERED_CAP

    override fun createItemStack(stackConsumer: Consumer<ItemStack>, lootContext: LootContext) {
        val state = lootContext.getParam(LootContextParams.BLOCK_STATE)
        val sporeAmount = state.getValue(ModProperties.SPORE_AMOUNT)
        stackConsumer.accept(ItemStack(
            ItemLoader.SPORE_POWDER,
            lootContext.random.nextIntBetweenInclusive(sporeAmount * 2, sporeAmount * 3)
        ))
        stackConsumer.accept(ItemStack(state.block.asItem()))
    }
}