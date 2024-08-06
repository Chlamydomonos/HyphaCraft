package xyz.chlamydomonos.hyphacraft.utils.misc

import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.material.Fluids

object CommonUtil {
    fun isNearFire(level: LevelReader, pos: BlockPos): Boolean {
        for (i in -3..3) {
            for (j in -3..3) {
                for (k in -3..3) {
                    val newPos = pos.offset(i, j, k)
                    val fluid = level.getFluidState(newPos)
                    if (fluid.`is`(Fluids.LAVA) || fluid.`is`(Fluids.FLOWING_LAVA)) {
                        return true
                    }
                    val state = level.getBlockState(newPos)
                    if (state.`is`(BlockTags.FIRE)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}