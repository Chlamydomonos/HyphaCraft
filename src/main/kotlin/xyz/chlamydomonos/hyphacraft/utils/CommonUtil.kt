package xyz.chlamydomonos.hyphacraft.utils

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.material.Fluids
import xyz.chlamydomonos.hyphacraft.utils.plant.MycovastusUtil
import xyz.chlamydomonos.hyphacraft.utils.plant.TumidusioUtil
import xyz.chlamydomonos.hyphacraft.utils.plant.XenolichenUtil

object CommonUtil {
    fun isNearFire(
        level: LevelReader,
        pos: BlockPos,
        center: Vec3i = Vec3i.ZERO,
        range: Vec3i = Vec3i(7, 7, 7)
    ): Boolean {
        val centerPos = pos.offset(center)
        val radiusX = (range.x - 1) / 2
        val radiusY = (range.y - 1) / 2
        val radiusZ = (range.z - 1) / 2

        for (i in -radiusX..radiusX) {
            for (j in -radiusY..radiusY) {
                for (k in -radiusZ..radiusZ) {
                    val newPos = centerPos.offset(i, j, k)
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

    fun tryExpandHypha(level: ServerLevel, pos: BlockPos) {
        if(XenolichenUtil.canGrow(level, pos)) {
            XenolichenUtil.setXenolichen(level, pos)
        } else if (MycovastusUtil.canHyphaGrow(level, pos)) {
            MycovastusUtil.setHypha(level, pos)
        } else if (TumidusioUtil.canHyphaGrow(level, pos)) {
            TumidusioUtil.setHypha(level, pos)
        }
    }
}