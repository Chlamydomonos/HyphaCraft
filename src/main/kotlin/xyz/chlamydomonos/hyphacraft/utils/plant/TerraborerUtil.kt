package xyz.chlamydomonos.hyphacraft.utils.plant

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Blocks
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.EntityLoader

object TerraborerUtil {
    const val INITIAL_GROW_RATE = 1.0f / 30.0f

    fun tryGrow(level: ServerLevel, pos: BlockPos) {
        var emptyBlocks = 0
        for (i in -3..3) {
            for (j in -3..0) {
                for (k in -3..3) {
                    if (level.getBlockState(pos.offset(i, j, k)).isEmpty) {
                        emptyBlocks++
                    }
                }
            }
        }

        if (emptyBlocks > 40) {
            return
        }

        if(level.getBlockState(pos).isEmpty) {
            level.setBlock(pos, BlockLoader.TERRABORER_STIPE.block.defaultBlockState(), 3)
        }
    }

    fun explode(level: ServerLevel, pos: BlockPos, random: RandomSource) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3)
        val entity = EntityLoader.TERRABORER_BOMB.create(level)!!
        val explosionTimes = random.nextIntBetweenInclusive(7, 40)
        entity.explosionsLeft = explosionTimes
        entity.setPos(pos.toVec3())
        level.addFreshEntity(entity)
    }
}