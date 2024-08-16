package xyz.chlamydomonos.hyphacraft.datagen

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.ModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.loaders.FluidLoader
import xyz.chlamydomonos.hyphacraft.loaders.ItemLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class ModItemModelProvider(
    output: PackOutput,
    existingFileHelper: ExistingFileHelper
) : ItemModelProvider(output, HyphaCraft.MODID, existingFileHelper) {
    override fun registerModels() {
        handheld(ItemLoader.DEBUG_STICK)
        basicItem(ItemLoader.XENOLICHEN)
        basicItem(ItemLoader.ROTTEN_FUNGUS_BALL)
        basicItem(ItemLoader.MOLDY_CORK_DUST)
        basicItem(ItemLoader.TUBULAR_HYPHA)
        basicItem(ItemLoader.WHITE_HYPHA)
        basicItem(ItemLoader.SPORE_POWDER)
        basicItem(ItemLoader.HUMUS)
        basicItem(ItemLoader.ALIEN_ORB)
        basicItem(FluidLoader.DIGESTIVE_JUICE.bucket)
        basicItem(ItemLoader.CARNIVORAVITIS_ROOT)
        basicItem(ItemLoader.CARNIVORAVITIS_FLOWER)
        basicItem(ItemLoader.TOXIC_SPORE_POWDER)
        basicItem(FluidLoader.ROTTEN_GOO.bucket)
        handheld(ItemLoader.BIOME_DEBUG_STICK, "debug_stick")
    }

    private fun handheld(item: Item, textureName: String? = null) {
        val name = BuiltInRegistries.ITEM.getKey(item)
        val texturePath = if (textureName != null) {
            NameUtil.getRL("${ModelProvider.ITEM_FOLDER}/${textureName}")
        } else {
            NameUtil.getRL("${ModelProvider.ITEM_FOLDER}/${name.path}")
        }
        getBuilder(name.toString())
            .parent(ModelFile.UncheckedModelFile("item/handheld"))
            .texture("layer0", texturePath)
    }
}