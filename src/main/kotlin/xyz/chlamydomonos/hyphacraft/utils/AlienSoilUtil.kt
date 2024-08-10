package xyz.chlamydomonos.hyphacraft.utils

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks
import xyz.chlamydomonos.hyphacraft.loaders.BlockTagLoader

object AlienSoilUtil {
    fun onAlienSoilPlaced(level: ServerLevel, pos: BlockPos) {
        if (level.getBlockState(pos.above()).`is`(BlockTagLoader.ALIEN_SOIL_DESTROYED)) {
            level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 3)
        }
    }
}