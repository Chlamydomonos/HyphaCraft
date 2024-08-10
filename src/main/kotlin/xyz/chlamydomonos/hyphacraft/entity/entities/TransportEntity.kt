package xyz.chlamydomonos.hyphacraft.entity.entities

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.plus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3i
import xyz.chlamydomonos.hyphacraft.blockentities.base.RouteBlockEntity
import xyz.chlamydomonos.hyphacraft.loaders.EntityLoader

class TransportEntity(entityType: EntityType<*>, level: Level) : Entity(entityType, level) {
    companion object {
        val BUILDER = EntityType.Builder.of(::TransportEntity, MobCategory.MISC).sized(0.01f, 0.01f)

        fun isOnTransport(entity: Entity?): Boolean {
            if (entity == null) {
                return false
            }
            if (entity.type == EntityLoader.TRANSPORT) {
                return true
            }
            var vehicle = entity.vehicle
            while (vehicle != null) {
                if (vehicle.type == EntityLoader.TRANSPORT) {
                    return true
                }
                vehicle = vehicle.vehicle
            }
            return false
        }
    }
    var ticksPerMovement = 1
    var blocksPerMovement = 1
    private var timer = 0

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        compound.putInt("ticks_per_movement", ticksPerMovement)
        compound.putInt("blocks_per_movement", blocksPerMovement)
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        ticksPerMovement = compound.getInt("ticks_per_movement")
        blocksPerMovement = compound.getInt("blocks_per_movement")
    }

    override fun tick() {
        super.tick()
        timer++
        if (timer >= ticksPerMovement) {
            timer = 0
            realTick()
        }
    }

    private fun realTick() {
        if (level().isClientSide) {
            return
        }

        val level = level() as ServerLevel
        val initialPos = BlockPos(position().toVec3i())
        var pos = initialPos
        var blockEntity = level.getBlockEntity(pos)
        var timesLeft = blocksPerMovement

        while (blockEntity is RouteBlockEntity && timesLeft > 0) {
            timesLeft--
            pos = blockEntity.getNextPos("") // TODO: route with data
            blockEntity = level.getBlockEntity(pos)
        }

        if (pos != initialPos) {
            moveTo(pos.toVec3() + Vec3(0.1, 0.1, 0.1))
        }

        if (blockEntity !is RouteBlockEntity) {
            unRide()
            discard()
        }
    }
}