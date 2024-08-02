package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.neoforged.neoforge.client.model.generators.ModelProvider
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class GrandisporiaWitheredCapBlock : Block(
    Properties.ofFullCopy(Blocks.DIRT)
        .sound(SoundType.FUNGUS)
        .randomTicks()
) {
    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.SPORE_AMOUNT, 0))
    }

    companion object {
        fun genModel(provider: ModBlockStateProvider) {
            provider.simpleBlockWithItem(BlockLoader.GRANDISPORIA_WITHERED_CAP.block, provider.models().cubeBottomTop(
                NameUtil.path(BlockLoader.GRANDISPORIA_WITHERED_CAP.block),
                NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/grandisporia_withered_cap"),
                NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/grandisporia_withered_cap_bottom"),
                NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/grandisporia_withered_cap")
            ))
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.SPORE_AMOUNT)
    }
}