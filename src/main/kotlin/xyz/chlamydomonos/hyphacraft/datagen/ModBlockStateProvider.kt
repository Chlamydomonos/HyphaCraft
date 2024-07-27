package xyz.chlamydomonos.hyphacraft.datagen

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.blocks.utils.HyphaCraftProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class ModBlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) :
    BlockStateProvider(output, HyphaCraft.MODID, exFileHelper) {
    override fun registerStatesAndModels() {
        simpleBlockWithItem(BlockLoader.ALIEN_ROCK.block, cubeAll(BlockLoader.ALIEN_ROCK.block))
        hyphaLike(BlockLoader.XENOLICHEN)
        hyphaLike(BlockLoader.XENOLICHEN_HIDDEN_BLOCK, "xenolichen")
        simpleBlockWithItem(BlockLoader.ALIEN_SOIL.block, cubeAll(BlockLoader.ALIEN_SOIL.block))
        simpleBlockWithItem(BlockLoader.HYPHACOTTA.block, cubeAll(BlockLoader.HYPHACOTTA.block))
    }

    private fun hyphaLike(block: Block) {
        val blockName = BuiltInRegistries.BLOCK.getKey(block).path
        hyphaLike(block, blockName)
    }

    private fun hyphaLike(block: Block, customName: String) {
        val models = List(3) {
            models().cubeAll(
                "${customName}_level_${it}",
                ResourceLocation.fromNamespaceAndPath(
                    HyphaCraft.MODID,
                    "${ModelProvider.BLOCK_FOLDER}/${customName}_level_${it}")
            ).renderType("translucent")
        }

        for(phase in 0..14) {
            getVariantBuilder(block).partialState().with(HyphaCraftProperties.PHASE, phase)
                .modelForState().modelFile(
                    if (phase < 5) models[0] else if (phase < 10) models[1] else models[2]
                ).addModel()
        }
    }
}