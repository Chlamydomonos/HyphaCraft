package xyz.chlamydomonos.hyphacraft.loot

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import xyz.chlamydomonos.hyphacraft.blockentities.BlockCopierEntity
import xyz.chlamydomonos.hyphacraft.loaders.LootLoader
import java.util.function.Consumer

class BlockCopierLoot(
    val item: Holder<Item>,
    val amount: Int,
    weight: Int,
    quality: Int,
    conditions: MutableList<LootItemCondition>,
    functions: MutableList<LootItemFunction>
) : LootPoolSingletonContainer(weight, quality, conditions, functions) {
    companion object {
        val CODEC = RecordCodecBuilder.mapCodec {
            it.group(
                BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item")
                    .forGetter { loot: BlockCopierLoot -> loot.item },
                Codec.INT.optionalFieldOf("amount", 1)
                    .forGetter { loot: BlockCopierLoot -> loot.amount }
            ).and(singletonFields(it)).apply(it, ::BlockCopierLoot)
        }

        @Suppress("DEPRECATION")
        fun builder(item: Item, amount: Int = 1) = simpleBuilder { w, q, c, f ->
            BlockCopierLoot(item.builtInRegistryHolder(), amount, w, q, c, f)
        }
    }

    override fun getType(): LootPoolEntryType {
        return LootLoader.BLOCK_COPIER
    }

    override fun createItemStack(stackConsumer: Consumer<ItemStack>, lootContext: LootContext) {
        val be = lootContext.getParam(LootContextParams.BLOCK_ENTITY) as BlockCopierEntity
        val block = be.copiedState.block
        if(amount > 0) {
            stackConsumer.accept(ItemStack(item, amount))
        }
        stackConsumer.accept(ItemStack(block.asItem()))
    }
}