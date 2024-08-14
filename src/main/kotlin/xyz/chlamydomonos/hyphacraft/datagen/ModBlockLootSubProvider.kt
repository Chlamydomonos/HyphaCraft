package xyz.chlamydomonos.hyphacraft.datagen

import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.storage.loot.IntRange
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer
import net.minecraft.world.level.storage.loot.functions.LimitCount
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.ItemLoader
import xyz.chlamydomonos.hyphacraft.loot.BlockCopierLoot
import xyz.chlamydomonos.hyphacraft.loot.SporeLoot
import java.util.stream.Collectors

class ModBlockLootSubProvider(
    provider: HolderLookup.Provider
):
    BlockLootSubProvider(emptySet(), FeatureFlags.REGISTRY.allFlags(), provider) {
    override fun generate() {
        dropSelf(BlockLoader.ALIEN_ROCK.block)
        dropSelf(BlockLoader.HYPHACOTTA.block)
        dropSelf(BlockLoader.ALIEN_SOIL.block)
        blockCopier(BlockLoader.XENOLICHEN_BLOCK, ItemLoader.XENOLICHEN)
        blockCopier(BlockLoader.MYCOVASTUS_HYPHA, Items.AIR, 0)
        candleLike(BlockLoader.MYCOVASTUS.block, ModProperties.MUSHROOM_COUNT)
        rateDrop(BlockLoader.ROTTEN_FUNGUS_HEAP.block, ItemLoader.ROTTEN_FUNGUS_BALL, -6.0f, 2.0f)
        rateDrop(BlockLoader.TUMIDUSIO_HYPHA, ItemLoader.MOLDY_CORK_DUST, -2.0f, 3.0f)
        candleLike(BlockLoader.TUMIDUSIO.block, ModProperties.DENSITY)
        rateDrop(BlockLoader.GRANDISPORIA_STIPE, ItemLoader.TUBULAR_HYPHA, -3.0f, 1.0f)
        rateDrop(BlockLoader.GRANDISPORIA_SMALL_CAP, ItemLoader.WHITE_HYPHA, -3.0f, 1.0f)
        rateDrop(BlockLoader.GRANDISPORIA_CAP_CENTER, ItemLoader.WHITE_HYPHA, -3.0f, 1.0f)
        rateDrop(BlockLoader.GRANDISPORIA_CAP, ItemLoader.WHITE_HYPHA, -3.0f, 1.0f)
        dropSelf(BlockLoader.GRANDISPORIA_WITHERED_STIPE.block)
        specified(BlockLoader.GRANDISPORIA_WITHERED_CAP.block, SporeLoot.builder(ItemLoader.SPORE_POWDER))
        rateDrop(BlockLoader.SPORE_HEAP.block, ItemLoader.SPORE_POWDER, 1.0f, 4.0f)
        rateDrop(BlockLoader.HUMUS_HEAP.block, ItemLoader.HUMUS, 1.0f, 6.0f)
        rateDrop(BlockLoader.LOOSE_FUNGUS_ROOT, ItemLoader.WHITE_HYPHA, 0.0f, 2.0f)
        dropSelf(BlockLoader.TERRABORER_STIPE.block)
        dropSelf(BlockLoader.TERRABORER_BOMB.block)
        dropSelf(BlockLoader.ACTIVE_HYPHA_BLOCK.block)
        dropSelf(BlockLoader.CARNIVORAVITIS_SHELL.block)
        dropOther(BlockLoader.CARNIVORAVITIS_FLOWER, ItemLoader.CARNIVORAVITIS_FLOWER)
        dropOther(BlockLoader.CARNIVORAVITIS_ROOT, ItemLoader.CARNIVORAVITIS_ROOT)
        rateDrop(BlockLoader.CARNIVORAVITIS_VINE, ItemLoader.TUBULAR_HYPHA, 0.0f, 1.0f)
        dropSelf(BlockLoader.HYPHACOAL_BLOCK.block)
        add(BlockLoader.ALIEN_SWARD.block) { createSingleItemTableWithSilkTouch(it, BlockLoader.ALIEN_SOIL.item) }
        add(BlockLoader.FERTILE_ALIEN_SWARD.block) { createSingleItemTableWithSilkTouch(it, BlockLoader.ALIEN_SOIL.item) }
        specified(BlockLoader.PULVERIUM.block, SporeLoot.builder(ItemLoader.TOXIC_SPORE_POWDER))
        dropSelf(BlockLoader.HARDENED_FUNGUS_SHELL.block)
    }

    override fun getKnownBlocks(): MutableIterable<Block> {
        return BlockLoader.BLOCKS_WITH_LOOT.stream().map { it.get() }.collect(Collectors.toList())
    }

    private fun blockCopier(block: Block, item: Item, amount: Int = 1) {
        val table = LootTable.lootTable().withPool(
            applyExplosionCondition(
                item,
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0f))
                    .add(BlockCopierLoot.builder(item, amount))
            )
        )
        add(block, table)
    }
    
    private fun candleLike(block: Block, property: IntegerProperty) {
        val table = LootTable.lootTable()
            .withPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0f))
                    .add(
                        applyExplosionDecay(
                            block,
                            LootItem.lootTableItem(block).apply(property.possibleValues.filter { it != 1 }) {
                                    SetItemCountFunction.setCount(ConstantValue.exactly(it.toFloat()))
                                        .`when`(
                                            LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(
                                                    StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(property, it)
                                                )
                                        )
                                }
                        )
                    )
            )

        add(block, table)
    }

    private fun rateDrop(block: Block, item: Item, minRate: Float, maxRate: Float) {
        val table = LootTable.lootTable().withPool(
            applyExplosionCondition(
                item,
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0f))
                    .add(
                        LootItem.lootTableItem(item)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(minRate, maxRate)))
                            .apply(LimitCount.limitCount(IntRange.lowerBound(0)))
                    )
            )
        )
        add(block, table)
    }

    private fun specified(block: Block, loot: LootPoolEntryContainer.Builder<*>) {
        val table = LootTable.lootTable().withPool(
            LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0f))
                .add(loot)
        )
        add(block, table)
    }
}