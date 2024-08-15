package xyz.chlamydomonos.hyphacraft.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.network.PacketDistributor
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockEntityLoader
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.DamageTypeLoader
import xyz.chlamydomonos.hyphacraft.loaders.NetworkLoader

class FulgurfungusBlockEntity(
    pos: BlockPos,
    blockState: BlockState
) : BlockEntity(BlockEntityLoader.FULGURFUNGUS, pos, blockState) {
    companion object {
        const val RANGE = 5.0
    }

    private var timer = 0
    fun tick() {
        if(level == null || level!!.isClientSide) {
            return
        }
        val serverLevel = level as ServerLevel

        timer++
        if (timer < 20) {
            return
        }
        timer = 0

        if (!blockState.getValue(ModProperties.ACTIVE)) {
            return
        }

        val aabb = AABB.ofSize(blockPos.center, RANGE, RANGE, RANGE)
        val entities = arrayListOf<LivingEntity>()
        serverLevel.entities.get(aabb) {
            if (it is LivingEntity) {
                entities.add(it)
            }
        }

        for (entity in entities) {
            shootEntity(serverLevel, entity)
        }
    }

    private fun shootEntity(level: ServerLevel, entity: LivingEntity) {
        val fromPos = blockPos.center.toVector3f()
        val toPos = entity.position().toVector3f()
        val packet = NetworkLoader.HYPHA_LIGHTNING.packet {
            it.from = fromPos
            it.to = toPos
        }
        PacketDistributor.sendToPlayersNear(
            level,
            null,
            blockPos.center.x,
            blockPos.center.y,
            blockPos.center.z,
            50.0,
            packet
        )
        entity.hurt(DamageSource(DamageTypeLoader.HYPHA_LIGHTNING(level)), 10.0f)
        level.setBlock(blockPos, BlockLoader.FULGURFUNGUS.block.defaultBlockState(), 3)
    }
}