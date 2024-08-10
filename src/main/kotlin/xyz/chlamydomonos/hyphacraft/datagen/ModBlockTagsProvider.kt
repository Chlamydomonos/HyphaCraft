package xyz.chlamydomonos.hyphacraft.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.loaders.BlockTagLoader
import java.util.concurrent.CompletableFuture

class ModBlockTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper
) : BlockTagsProvider(output, lookupProvider, HyphaCraft.MODID, existingFileHelper) {
    override fun addTags(p0: HolderLookup.Provider) {
        for (tagDef in BlockTagLoader.TAGS) {
            for (blockOrTag in tagDef.blocks()) {
                if (blockOrTag.isBlock) {
                    tag(tagDef.key).add(blockOrTag.block!!)
                } else {
                    tag(tagDef.key).addTag(blockOrTag.tag!!)
                }
            }
        }
    }
}