package xyz.chlamydomonos.hyphacraft.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EntityTypeTagsProvider
import net.minecraft.tags.EntityTypeTags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.loaders.EntityLoader
import java.util.concurrent.CompletableFuture

class ModEntityTagsProvider(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper?
) : EntityTypeTagsProvider(output, provider, HyphaCraft.MODID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
        tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(EntityLoader.TRANSPORT)
    }
}