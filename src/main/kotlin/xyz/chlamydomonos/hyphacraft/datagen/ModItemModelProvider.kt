package xyz.chlamydomonos.hyphacraft.datagen

import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.loaders.ItemLoader

class ModItemModelProvider(
    output: PackOutput,
    existingFileHelper: ExistingFileHelper
) : ItemModelProvider(output, HyphaCraft.MODID, existingFileHelper) {
    override fun registerModels() {
        basicItem(ItemLoader.DEBUG_STICK)
        basicItem(ItemLoader.XENOLICHEN)
        basicItem(ItemLoader.ROTTEN_FUNGUS_BALL)
        basicItem(ItemLoader.MOLDY_CORK_DUST)
    }

}