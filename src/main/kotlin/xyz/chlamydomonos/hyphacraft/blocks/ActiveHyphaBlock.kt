package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.MapColor
import xyz.chlamydomonos.hyphacraft.blocks.base.ImmuneToHyphaExplosionBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.DataAttachmentLoader
import xyz.chlamydomonos.hyphacraft.utils.plant.CarnivoravitisUtil

class ActiveHyphaBlock : ImmuneToHyphaExplosionBlock(
    Properties.ofFullCopy(Blocks.DIRT)
        .randomTicks()
        .sound(SoundType.SLIME_BLOCK)
        .mapColor(MapColor.COLOR_BLUE)
        .ignitedByLava()
) {
    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (random.nextFloat() < CarnivoravitisUtil.EXPAND_RATE) {
            CarnivoravitisUtil.tryGrowInitial(level, pos)
            if (level.getBlockState(pos.above()).isEmpty) {
                val chunk = level.getChunkAt(pos)
                if (chunk.getData(DataAttachmentLoader.IS_ALIEN_FOREST)) {
                    level.setBlock(pos.above(), BlockLoader.VERMILINGUA.defaultBlockState(), 3)
                }
            }
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