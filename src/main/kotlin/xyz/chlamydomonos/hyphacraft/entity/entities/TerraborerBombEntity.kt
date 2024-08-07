package xyz.chlamydomonos.hyphacraft.entity.entities

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.MoverType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.Fluids
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import xyz.chlamydomonos.hyphacraft.loaders.BlockTagLoader
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.DamageTypeLoader
import xyz.chlamydomonos.hyphacraft.utils.plant.XenolichenUtil
import kotlin.math.roundToInt

class TerraborerBombEntity(entityType: EntityType<TerraborerBombEntity>, level: Level) : Entity(entityType, level) {
    companion object {
        private val EXPLOSIONS_LEFT = SynchedEntityData.defineId(
            TerraborerBombEntity::class.java,
            EntityDataSerializers.INT
        )

        val BUILDER = EntityType.Builder.of(::TerraborerBombEntity, MobCategory.MISC)
            .sized(0.98f, 0.98f).fireImmune()
    }

    var explosionsLeft
        get() = entityData.get(EXPLOSIONS_LEFT)
        set(value) { entityData.set(EXPLOSIONS_LEFT, value) }

    var ticks = 0

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder.define(EXPLOSIONS_LEFT, 5)
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        explosionsLeft = compound.getInt("explosions_left")
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        compound.putInt("explosions_left", explosionsLeft)
    }

    override fun tick() {
        super.tick()
        applyGravity()
        move(MoverType.SELF, deltaMovement)
        if (level().isClientSide) {
            return
        }

        ticks++

        if (ticks == 10) {
            ticks = 0
            if (onGround()) {
                val level = level() as ServerLevel
                val radius = random.nextIntBetweenInclusive(5, 9)
                level.explode(
                    this,
                    DamageSource(DamageTypeLoader.HYPHA_EXPLOSION(level), this),
                    null,
                    x,
                    y,
                    z,
                    radius.toFloat(),
                    false,
                    Level.ExplosionInteraction.BLOCK
                )

                val lavaRadius = (radius * 1.5).roundToInt()

                for (i in x.roundToInt() - lavaRadius..x.roundToInt() + lavaRadius) {
                    for (j in y.roundToInt() - lavaRadius..y.roundToInt() + lavaRadius) {
                        for (k in z.roundToInt() - lavaRadius..z.roundToInt() + lavaRadius) {
                            val distance = Vec3i(i, j, k).toVec3().distanceTo(position())
                            if (distance <= lavaRadius) {
                                val newPos = BlockPos(i, j, k)
                                val fluid = level.getFluidState(newPos)
                                val state = level.getBlockState(newPos)
                                if (fluid.`is`(Fluids.LAVA) || fluid.`is`(Fluids.FLOWING_LAVA)) {
                                    level.setBlock(newPos, BlockLoader.HYPHACOTTA.block.defaultBlockState(), 3)
                                }
                                if (
                                    state.`is`(BlockTagLoader.HYPHA_TREE) && (
                                        (
                                            state.hasProperty(BlockStateProperties.DOWN) &&
                                            state.getValue(BlockStateProperties.DOWN)
                                        ) || (
                                            !state.hasProperty(BlockStateProperties.DOWN)
                                        )
                                    )
                                ) {
                                    var tempPos = newPos.below()
                                    while (level().getBlockState(tempPos).isEmpty) {
                                        level.setBlock(
                                            tempPos,
                                            BlockLoader.LOOSE_FUNGUS_ROOT.defaultBlockState(),
                                            3
                                        )
                                        tempPos = tempPos.below()
                                    }
                                }
                                if (distance <= radius * 1.2) {
                                    if (fluid.`is`(Fluids.WATER) || fluid.`is`(Fluids.FLOWING_WATER)) {
                                        if (random.nextBoolean()) {
                                            level.setBlock(
                                                newPos,
                                                BlockLoader.ACTIVE_HYPHA_BLOCK.block.defaultBlockState(),
                                                3
                                            )
                                        } else {
                                            level.setBlock(newPos, Blocks.AIR.defaultBlockState(), 3)
                                        }
                                    }
                                    if (
                                        explosionsLeft <= 1 &&
                                        !level.getBlockState(newPos).isEmpty &&
                                        level.getBlockState(newPos.above()).isEmpty
                                    ) {
                                        level.setBlock(
                                            newPos,
                                            BlockLoader.ACTIVE_HYPHA_BLOCK.block.defaultBlockState(),
                                            3
                                        )
                                    } else if(random.nextInt(8) == 0 && XenolichenUtil.canGrow(level, newPos)) {
                                        XenolichenUtil.setXenolichen(level, newPos)
                                    }
                                }
                            }
                        }
                    }
                }

                explosionsLeft--
                if (explosionsLeft <= 0) {
                    discard()
                }
            }
        }
    }

    override fun isNoGravity() = false

    override fun getDefaultGravity() = 0.04
}