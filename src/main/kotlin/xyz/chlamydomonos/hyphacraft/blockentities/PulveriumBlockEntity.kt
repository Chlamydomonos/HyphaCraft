package xyz.chlamydomonos.hyphacraft.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.plus
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockEntityLoader
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.EntityLoader
import xyz.chlamydomonos.hyphacraft.utils.plant.PulveriumUtil

class PulveriumBlockEntity(
    pos: BlockPos,
    blockState: BlockState
) : BlockEntity(BlockEntityLoader.PULVERIUM, pos, blockState) {
    companion object {
        const val RANGE = 5.0
    }

    private var timer = 0

    var blowing = false
        set(value) {
            field = value
            setChanged()
        }

    fun tick() {
        if (blowing) {
            return
        }

        if(level == null || level!!.isClientSide) {
            return
        }
        val serverLevel = level as ServerLevel

        timer++
        if (timer < 20) {
            return
        }

        val sporeAmount = blockState.getValue(ModProperties.SPORE_AMOUNT)
        if (sporeAmount == 0) {
            return
        }

        val aabb = AABB.ofSize(blockPos.center, RANGE, RANGE, RANGE)
        var hasEntity = false
        serverLevel.entities.get(aabb) {
            if (it is LivingEntity) {
                if (it !is Player) {
                    hasEntity = true
                } else if (!it.isSpectator) {
                    if (!it.isCreative) {
                        hasEntity = true
                    } else if (PulveriumUtil.AFFECT_CREATIVE_PLAYER) {
                        hasEntity = true
                    }
                }
            }
        }

        if (hasEntity) {
            blowing = true
            val entity = EntityLoader.SPORE_CLOUD.create(serverLevel)!!
            entity.setPos(blockPos.center + Vec3(0.0, RANGE / 2, 0.0))
            entity.ticksLeft = sporeAmount * 30
            entity.range = RANGE.toFloat()
            serverLevel.addFreshEntity(entity)
            serverLevel.scheduleTick(blockPos, BlockLoader.PULVERIUM.block, 30)
        }
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.putBoolean("blowing", blowing)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        blowing = tag.getBoolean("blowing")
    }
}