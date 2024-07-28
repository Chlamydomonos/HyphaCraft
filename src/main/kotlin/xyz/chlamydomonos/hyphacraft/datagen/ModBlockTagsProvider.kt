package xyz.chlamydomonos.hyphacraft.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import java.util.concurrent.CompletableFuture

class ModBlockTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper?
) : BlockTagsProvider(output, lookupProvider, HyphaCraft.MODID, existingFileHelper) {
    override fun addTags(p0: HolderLookup.Provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BlockLoader.ALIEN_ROCK.block)
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(BlockLoader.ALIEN_SOIL.block)
    }
}