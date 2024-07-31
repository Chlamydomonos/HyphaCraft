package xyz.chlamydomonos.hyphacraft.utils.plant

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

object GrandisporiaUtil {
    const val INITIAL_GROW_RATE = 1.0f / 30.0f

    fun tryGrowInitialStipe(level: ServerLevel, pos: BlockPos) {
        if (!level.getBlockState(pos).isEmpty) {
            return
        }
        for (i in -5..5) {
            for (j in -5..5) {
                for (k in -5..5) {
                    val state = level.getBlockState(pos.offset(i, j, k))
                    if (state.`is`(BlockLoader.GRANDISPORIA_STIPE) || state.`is`(BlockLoader.GRANDISPORIA_WITHERED_STIPE.block)) {
                        return
                    }
                }
            }
        }
        level.setBlock(pos, BlockLoader.GRANDISPORIA_STIPE.defaultBlockState(), 3)
    }
}