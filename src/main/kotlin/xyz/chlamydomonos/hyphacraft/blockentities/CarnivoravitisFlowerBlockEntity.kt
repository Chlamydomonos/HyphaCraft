package xyz.chlamydomonos.hyphacraft.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.plus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.times
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockEntityLoader

class CarnivoravitisFlowerBlockEntity(
    pos: BlockPos,
    blockState: BlockState
) : BlockEntity(BlockEntityLoader.CARNIVORAVITIS_FLOWER, pos, blockState) {
    companion object {
        const val CAPTURE_RANGE = 5.0
    }

    private var timer = 0

    private fun devour(entity: LivingEntity) {
        TODO()
    }

    fun tick() {
        if(level == null || level!!.isClientSide) {
            return
        }

        timer++
        if (timer < 20) {
            return
        }
        timer = 0

        val serverLevel = level as ServerLevel
        val direction = blockState.getValue(ModProperties.DIRECTION)
        val aabbCenter = blockPos.toVec3() + direction.normal.toVec3() * (CAPTURE_RANGE / 2)
        val aabb = AABB.ofSize(aabbCenter, CAPTURE_RANGE, CAPTURE_RANGE, CAPTURE_RANGE)
        val entities = arrayListOf<LivingEntity>()

        serverLevel.entities.get(aabb) {
            if (it is LivingEntity) {
                entities.add(it)
            }
        }

        if (entities.isEmpty()) {
            return
        }

        val randomId = serverLevel.random.nextInt(entities.size)
        devour(entities[randomId])
    }
}