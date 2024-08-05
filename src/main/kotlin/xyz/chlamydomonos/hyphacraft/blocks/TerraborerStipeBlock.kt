package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ModelProvider
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class TerraborerStipeBlock : Block(
    Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion().randomTicks().instabreak().noCollission()
), BurnableHypha {
    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.AGE, 0))
    }

    companion object {
        fun genModel(provider: ModBlockStateProvider) {
            val name = NameUtil.path(BlockLoader.TERRABORER_STIPE.block)
            val model0 = provider.models().cross(
                "${name}_0",
                NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/${name}_0")
            ).renderType("cutout")
            val model1 = provider.models().cross(
                "${name}_1",
                NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/${name}_1")
            ).renderType("cutout")
            for (age in 0..3) {
                provider.getVariantBuilder(BlockLoader.TERRABORER_STIPE.block).partialState()
                    .with(ModProperties.AGE, age)
                    .setModels(ConfiguredModel(if (age < 2) model0 else model1))
            }
            provider.simpleBlockItem(BlockLoader.TERRABORER_STIPE.block, model0)
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.AGE)
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val age = state.getValue(ModProperties.AGE)
        if (age < 3) {
            level.setBlock(pos, state.cycle(ModProperties.AGE), 3)
        } else if (level.getBlockState(pos.above()).isEmpty && random.nextInt(5) == 0) {
            level.setBlock(pos.above(), BlockLoader.TERRABORER_BOMB.block.defaultBlockState(), 3)
        }
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        val belowState = level.getBlockState(pos.below())
        return belowState.`is`(BlockLoader.ALIEN_SOIL.block)
    }
}