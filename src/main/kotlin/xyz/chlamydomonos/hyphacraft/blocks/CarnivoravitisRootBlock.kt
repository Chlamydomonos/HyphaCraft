package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.ModelProvider
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaBlock
import xyz.chlamydomonos.hyphacraft.blocks.base.BurnableHypha
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class CarnivoravitisRootBlock : BaseHyphaBlock(Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.FUNGUS)) {
    companion object {
        fun genModel(provider: ModBlockStateProvider) {
            provider.simpleBlockWithItem(
                BlockLoader.CARNIVORAVITIS_ROOT,
                provider.models().cubeColumn(
                    "carnivoravitis_root",
                    NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/carnivoravitis_root"),
                    NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/carnivoravitis_root_top")
                )
            )
        }
    }

    override fun onBurnt(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        replacing: Boolean,
        random: RandomSource
    ): BurnableHypha.VanillaBehaviourHandler {
        level.setBlock(pos, BlockLoader.HYPHACOAL_BLOCK.block.defaultBlockState(), 3)
        return BurnableHypha.VanillaBehaviourHandler.CANCEL
    }
}