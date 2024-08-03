package xyz.chlamydomonos.hyphacraft.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModBlockTags
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
        tag(BlockTags.MINEABLE_WITH_HOE).add(BlockLoader.TUMIDUSIO.block)
        tag(BlockTags.MINEABLE_WITH_HOE).add(BlockLoader.TUMIDUSIO_HYPHA)
        tag(BlockTags.MINEABLE_WITH_HOE).add(BlockLoader.XENOLICHEN_BLOCK)
        tag(BlockTags.MINEABLE_WITH_HOE).add(BlockLoader.MYCOVASTUS_HYPHA)
        tag(ModBlockTags.PRESERVE_LEAVES).add(BlockLoader.TUMIDUSIO_HYPHA)
        tag(BlockTags.MINEABLE_WITH_HOE).add(BlockLoader.GRANDISPORIA_STIPE)
        tag(BlockTags.MINEABLE_WITH_HOE).add(BlockLoader.GRANDISPORIA_SMALL_CAP)
        tag(BlockTags.MINEABLE_WITH_HOE).add(BlockLoader.GRANDISPORIA_CAP_CENTER)
        tag(BlockTags.MINEABLE_WITH_HOE).add(BlockLoader.GRANDISPORIA_CAP)
        tag(BlockTags.MINEABLE_WITH_AXE).add(BlockLoader.GRANDISPORIA_WITHERED_STIPE.block)
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BlockLoader.GRANDISPORIA_WITHERED_CAP.block)
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(BlockLoader.ROTTEN_FUNGUS_HEAP.block)
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(BlockLoader.SPORE_HEAP.block)
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(BlockLoader.HUMUS_HEAP.block)
    }
}