package xyz.chlamydomonos.hyphacraft.utils.plant

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.CommonUtil

object PulveriumUtil {
    const val GROWTH_RATE = 1.0f / 20.0f
    fun tryGrow(level: ServerLevel, pos: BlockPos) {
        if (CommonUtil.isNearFire(level, pos)) {
            return
        }

        for (i in -6..6) {
            for (j in -6..6) {
                for (k in -6..6) {
                    if (level.getBlockState(pos).`is`(BlockLoader.PULVERIUM.block)) {
                        return
                    }
                }
            }
        }

        level.setBlock(pos, BlockLoader.PULVERIUM.block.defaultBlockState(), 3)
    }
}