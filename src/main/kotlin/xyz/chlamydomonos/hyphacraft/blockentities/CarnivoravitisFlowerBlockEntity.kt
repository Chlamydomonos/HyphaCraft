package xyz.chlamydomonos.hyphacraft.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.plus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.times
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.entity.entities.TransportEntity
import xyz.chlamydomonos.hyphacraft.loaders.BlockEntityLoader
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.EntityLoader
import xyz.chlamydomonos.hyphacraft.utils.plant.CarnivoravitisUtil

class CarnivoravitisFlowerBlockEntity(
    pos: BlockPos,
    blockState: BlockState
) : BlockEntity(BlockEntityLoader.CARNIVORAVITIS_FLOWER, pos, blockState) {
    companion object {
        const val CAPTURE_RANGE = 5.0
        const val CARRIER_BLOCKS_PER_MOVEMENT = 1
        const val CARRIER_TICKS_PER_MOVEMENT = 1
    }

    private var timer = 0

    private fun devour(entity: Entity) {
        val carrier = EntityLoader.TRANSPORT.create(level!!)!!
        val vinePos = blockPos.offset(blockState.getValue(ModProperties.DIRECTION).opposite.normal)
        val vineState = level!!.getBlockState(vinePos)
        if (!vineState.`is`(BlockLoader.CARNIVORAVITIS_VINE)) {
            return
        }
        val carrierPos = vinePos.toVec3() + Vec3(0.1, 0.1, 0.1)
        carrier.setPos(carrierPos)
        carrier.blocksPerMovement = CARRIER_BLOCKS_PER_MOVEMENT
        carrier.ticksPerMovement = CARRIER_TICKS_PER_MOVEMENT
        level!!.addFreshEntity(carrier)
        entity.setPos(carrierPos)
        entity.startRiding(carrier, true)
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
        val entities = arrayListOf<Entity>()

        serverLevel.entities.get(aabb) {
            if (it is LivingEntity && !TransportEntity.isOnTransport(it)) {
                if (it !is Player) {
                    entities.add(it.rootVehicle)
                } else if (!it.isSpectator) {
                    if (!it.isCreative) {
                        entities.add(it.rootVehicle)
                    } else if (CarnivoravitisUtil.AFFECT_CREATIVE_PLAYER) {
                        entities.add(it.rootVehicle)
                    }
                }
            }
        }

        if (entities.isEmpty()) {
            return
        }

        val randomId = serverLevel.random.nextInt(entities.size)
        devour(entities[randomId])
    }
}