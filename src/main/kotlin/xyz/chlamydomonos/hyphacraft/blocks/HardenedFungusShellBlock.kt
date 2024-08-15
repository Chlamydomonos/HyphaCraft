package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.DataAttachmentLoader

class HardenedFungusShellBlock : BaseHyphaBlock(
    Properties.ofFullCopy(Blocks.OAK_PLANKS).randomTicks().sound(SoundType.FUNGUS)
) {
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

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (!level.getChunkAt(pos).getData(DataAttachmentLoader.IS_ALIEN_FOREST)) {
            return
        }

        for (i in -1..1) {
            for (j in -1..1) {
                for (k in -1..1) {
                    val newPos = pos.offset(i, j, k)
                    if (level.getBlockState(newPos).`is`(BlockLoader.TUMIDUSIO.block) && random.nextBoolean()) {
                        var hasEmptyNeighbor = false
                        for (direction in Direction.entries) {
                            val newPos2 = newPos.offset(direction.normal)
                            if (level.getBlockState(newPos2).isEmpty) {
                                hasEmptyNeighbor = true
                                break
                            }
                        }
                        if (hasEmptyNeighbor) {
                            level.setBlock(newPos, defaultBlockState(), 3)
                        }
                    }
                }
            }
        }
    }
}