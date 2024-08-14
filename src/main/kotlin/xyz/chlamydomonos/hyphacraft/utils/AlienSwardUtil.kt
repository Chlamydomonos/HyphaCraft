package xyz.chlamydomonos.hyphacraft.utils

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.DataAttachmentLoader

object AlienSwardUtil {
    fun trySpread(level: ServerLevel, pos: BlockPos, random: RandomSource) {
        for (i in -3..3) {
            for (j in -3..3) {
                for (k in -3..3) {
                    val newPos = pos.offset(i, j, k)
                    if (!level.getBlockState(newPos).`is`(BlockLoader.ALIEN_SOIL.block)) {
                        continue
                    }
                    if (random.nextInt(5) != 0) {
                        continue
                    }
                    if (!level.getChunkAt(newPos).getData(DataAttachmentLoader.IS_ALIEN_FOREST)) {
                        continue
                    }
                    if (level.getBlockState(newPos.above()).isEmpty) {
                        level.setBlock(newPos, BlockLoader.ALIEN_SWARD.block.defaultBlockState(), 3)
                    }
                }
            }
        }
    }

    fun canSustain(level: ServerLevel, pos: BlockPos): Boolean {
        val state = level.getBlockState(pos.above())
        return !Block.isFaceFull(state.getCollisionShape(level, pos.above()), Direction.DOWN)
    }
}