package xyz.chlamydomonos.hyphacraft.datagen

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.blocks.GrandisporiaCapBlock
import xyz.chlamydomonos.hyphacraft.blocks.GrandisporiaCapCenterBlock
import xyz.chlamydomonos.hyphacraft.blocks.GrandisporiaStipeBlock
import xyz.chlamydomonos.hyphacraft.blocks.GrandisporiaWitheredCapBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class ModBlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) :
    BlockStateProvider(output, HyphaCraft.MODID, exFileHelper) {
    override fun registerStatesAndModels() {
        simpleBlock(BlockLoader.TEST_BLOCK, cubeAll(BlockLoader.TEST_BLOCK))
        simpleBlockWithItem(BlockLoader.ALIEN_ROCK.block, cubeAll(BlockLoader.ALIEN_ROCK.block))
        blockCopier(BlockLoader.XENOLICHEN_BLOCK)
        blockCopier(BlockLoader.XENOLICHEN_HIDDEN_BLOCK, "xenolichen_block")
        simpleBlockWithItem(BlockLoader.ALIEN_SOIL.block, cubeAll(BlockLoader.ALIEN_SOIL.block))
        simpleBlockWithItem(BlockLoader.HYPHACOTTA.block, cubeAll(BlockLoader.HYPHACOTTA.block))
        blockCopier(BlockLoader.MYCOVASTUS_HYPHA)
        blockCopier(BlockLoader.MYCOVASTUS_HYPHA_HIDDEN_BLOCK, "mycovastus_hypha")
        candleLike(BlockLoader.MYCOVASTUS.block, ModProperties.MUSHROOM_COUNT)
        simpleBlockItem(BlockLoader.MYCOVASTUS.block, existingModel("mycovastus_1"))
        simpleBlockWithItem(BlockLoader.ROTTEN_FUNGUS_HEAP.block, carpet(BlockLoader.ROTTEN_FUNGUS_HEAP.block))
        blockCopier(BlockLoader.TUMIDUSIO_HYPHA)
        blockCopier(BlockLoader.TUMIDUSIO_HYPHA_HIDDEN_BLOCK, "tumidusio_hypha")
        simpleBlockWithItem(BlockLoader.HYPHACOAL_BLOCK.block, cubeAll(BlockLoader.HYPHACOAL_BLOCK.block))
        simpleBlockWithItem(BlockLoader.TUMIDUSIO.block, cubeAll(BlockLoader.TUMIDUSIO.block))
        GrandisporiaStipeBlock.genModel(this)
        simpleBlock(BlockLoader.GRANDISPORIA_SMALL_CAP, existingModel("grandisporia_small_cap"))
        GrandisporiaCapCenterBlock.genModel(this)
        GrandisporiaCapBlock.genModel(this)
        simpleBlockWithItem(BlockLoader.GRANDISPORIA_WITHERED_STIPE.block, models().cubeColumn(
            "grandisporia_withered_stipe",
            NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/grandisporia_withered_stipe"),
            NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/grandisporia_withered_stipe_top")
        ))
        GrandisporiaWitheredCapBlock.genModel(this)
    }

    private fun blockCopier(block: Block) {
        val blockName = BuiltInRegistries.BLOCK.getKey(block).path
        blockCopier(block, blockName)
    }

    private fun blockCopier(block: Block, customName: String) {
        val models = List(3) {
            models().cubeAll(
                "${customName}_level_${it}",
                NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/${customName}_level_${it}")
            ).renderType("translucent")
        }

        for(phase in 0..14) {
            getVariantBuilder(block).partialState().with(ModProperties.PHASE, phase)
                .modelForState().modelFile(
                    if (phase < 5) models[0] else if (phase < 10) models[1] else models[2]
                ).addModel()
        }
    }

    private fun candleLike(block: Block, property: IntegerProperty) {
        for(count in property.possibleValues) {
            getVariantBuilder(block).partialState().with(property, count)
                .setModels(ConfiguredModel(
                    models().getExistingFile(
                        NameUtil.getRL(
                            "${ModelProvider.BLOCK_FOLDER}/${BuiltInRegistries.BLOCK.getKey(block).path}_$count"
                        )
                    )
                ))
        }
    }

    fun existingModel(name: String) = models().getExistingFile(
        NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/$name")
    )

    private fun carpet(block: Block) = models().carpet(NameUtil.path(block), blockTexture(block))
}