package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.ModelProvider
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaBlock
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.DataAttachmentLoader
import xyz.chlamydomonos.hyphacraft.utils.AlienSwardUtil
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class AlienSwardBlock : BaseHyphaBlock(
    Properties.ofFullCopy(Blocks.DIRT).randomTicks().sound(SoundType.SLIME_BLOCK)
) {
    companion object {
        fun genModel(provider: ModBlockStateProvider) {
            val name = NameUtil.path(BlockLoader.ALIEN_SWARD.block)
            val model = provider.models().cubeBottomTop(
                name,
                NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/alien_sward_side"),
                NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/alien_soil"),
                NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/alien_sward_top")
            )
            provider.simpleBlockWithItem(BlockLoader.ALIEN_SWARD.block, model)
        }
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (!AlienSwardUtil.canSustain(level, pos)) {
            level.setBlock(pos, BlockLoader.ALIEN_SOIL.block.defaultBlockState(), 3)
        } else if (level.getChunkAt(pos).getData(DataAttachmentLoader.IS_ALIEN_FOREST)) {
            AlienSwardUtil.trySpread(level, pos, random)
        }
    }
}