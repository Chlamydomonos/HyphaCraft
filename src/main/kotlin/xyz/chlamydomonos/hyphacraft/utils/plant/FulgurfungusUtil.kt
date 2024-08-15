package xyz.chlamydomonos.hyphacraft.utils.plant

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.CommonUtil

object FulgurfungusUtil {
    const val GROWTH_RATE = 1.0f / 20.0f
    fun tryGrow(level: ServerLevel, pos: BlockPos) {
        if (CommonUtil.isNearFire(level, pos)) {
            return
        }

        for (i in -10..10) {
            for (j in -6..6) {
                for (k in -10..10) {
                    if (level.getBlockState(pos.offset(i, j, k)).`is`(BlockLoader.FULGURFUNGUS.block)) {
                        return
                    }
                }
            }
        }

        level.setBlock(pos, BlockLoader.FULGURFUNGUS.block.defaultBlockState(), 3)
    }
}