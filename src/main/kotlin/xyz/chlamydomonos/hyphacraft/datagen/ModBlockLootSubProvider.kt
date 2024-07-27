package xyz.chlamydomonos.hyphacraft.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class ModBlockLootSubProvider(provider: HolderLookup.Provider):
    BlockLootSubProvider(emptySet(), FeatureFlags.REGISTRY.allFlags(), provider) {
    override fun generate() {
        dropSelf(BlockLoader.ALIEN_ROCK.block)
        dropSelf(BlockLoader.HYPHACOTTA.block)
        dropSelf(BlockLoader.ALIEN_SOIL.block)
    }

    override fun getKnownBlocks(): MutableIterable<Block> {
        return mutableSetOf(
            BlockLoader.ALIEN_ROCK.block,
            BlockLoader.HYPHACOTTA.block,
            BlockLoader.ALIEN_SOIL.block
        )
    }
}