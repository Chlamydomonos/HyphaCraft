package xyz.chlamydomonos.hyphacraft.entity.entities

import net.minecraft.core.particles.ColorParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import xyz.chlamydomonos.hyphacraft.entity.ModEntityTags
import xyz.chlamydomonos.hyphacraft.loaders.EffectLoader
import xyz.chlamydomonos.hyphacraft.utils.ColorUtil
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.roundToInt

class SporeCloudEntity(entityType: EntityType<*>, level: Level) : Entity(entityType, level) {
    companion object {
        private val TICKS_LEFT = SynchedEntityData.defineId(
            SporeCloudEntity::class.java,
            EntityDataSerializers.INT
        )

        private val RANGE = SynchedEntityData.defineId(
            SporeCloudEntity::class.java,
            EntityDataSerializers.FLOAT
        )

        val BUILDER = EntityType.Builder.of(::SporeCloudEntity, MobCategory.MISC).fireImmune()

        val PARTICLE = ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, ColorUtil.rgba(0x513560ff))
    }

    var ticksLeft
        get() = entityData.get(TICKS_LEFT)
        set(value) { entityData.set(TICKS_LEFT, value) }

    var range
        get() = entityData.get(RANGE)
        set(value) { entityData.set(RANGE, value) }

    private var timer = 0

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder.define(TICKS_LEFT, 20)
        builder.define(RANGE, 1.0f)
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        ticksLeft = compound.getInt("ticks_left")
        range = compound.getFloat("range")
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        compound.putInt("ticks_left", ticksLeft)
        compound.putFloat("range", range)
    }

    override fun tick() {
        super.tick()
        if (level().isClientSide) {
            val particleCount = log2(ticksLeft + 2.0f) * range.pow(3) * 20
            for (i in 1..particleCount.roundToInt()) {
                level().addParticle(
                    PARTICLE,
                    x + random.nextGaussian() * range,
                    y + random.nextGaussian() * range,
                    z + random.nextGaussian() * range,
                    0.0,
                    0.0,
                    0.0
                )
            }
            return
        }

        ticksLeft--
        if (ticksLeft <= 0) {
            discard()
        }

        timer++
        if (timer < 20) {
            return
        }
        timer = 0

        val aabb = AABB.ofSize(position(), range.toDouble(), range.toDouble(), range.toDouble())
        (level() as ServerLevel).entities.get(aabb) {
            if (it is LivingEntity) {
                val effect1 = MobEffectInstance(MobEffects.POISON, 600, 1)
                val effect2 = MobEffectInstance(EffectLoader.COVERED_WITH_SPORE, 300, 2)
                if (!it.type.`is`(ModEntityTags.HYPHACRAFT_INSECT)) {
                    it.addEffect(effect1)
                }
                it.addEffect(effect2)
            }
        }
    }
}