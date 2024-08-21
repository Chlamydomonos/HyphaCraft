package xyz.chlamydomonos.hyphacraft.entity.entities

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.level.Level
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.RawAnimation
import software.bernie.geckolib.util.GeckoLibUtil
import xyz.chlamydomonos.hyphacraft.entity.entities.HumifossorEntity.Companion.State.*
import xyz.chlamydomonos.hyphacraft.entity.goals.HumifossorHideGoal

class HumifossorEntity(
    entityType: EntityType<out PathfinderMob>,
    level: Level
) : PathfinderMob(entityType, level), GeoEntity {
    companion object {
        val BUILDER = EntityType.Builder.of(::HumifossorEntity, MobCategory.CREATURE)
        fun mobBuilder() = createMobAttributes()
            .add(Attributes.MAX_HEALTH, 10.0)
            .add(Attributes.MOVEMENT_SPEED, 0.2)

        val WALK_ANIM = RawAnimation.begin().thenLoop("walk")
        val CURL_ANIM = RawAnimation.begin().thenPlayAndHold("curl")
        val ROLL_ANIM = RawAnimation.begin().thenLoop("roll")
        val EXPAND_ANIM = RawAnimation.begin().thenPlayAndHold("expand")
        val IDLE_ANIM = RawAnimation.begin().thenLoop("idle")
        val HIDE_ANIM = RawAnimation.begin().thenLoop("hide")

        enum class State(val id: Byte) {
            BASE(0),
            HIDING(1),
            CURLING(2),
            EXPANDING(3)
        }
        private val STATE = SynchedEntityData.defineId(
            HumifossorEntity::class.java,
            EntityDataSerializers.BYTE
        )
    }

    private val geoCache = GeckoLibUtil.createInstanceCache(this)

    var state
        get() = State.entries[entityData.get(STATE).toInt()]
        set(value) { entityData.set(STATE, value.id) }

    val controller = AnimationController(this, "moving", 1) {
        when (state) {
            BASE -> if (it.isMoving) it.setAndContinue(WALK_ANIM) else it.setAndContinue(IDLE_ANIM)
            HIDING -> if (it.isMoving) it.setAndContinue(ROLL_ANIM) else it.setAndContinue(HIDE_ANIM)
            CURLING -> it.setAndContinue(CURL_ANIM)
            EXPANDING -> it.setAndContinue(EXPAND_ANIM)
        }
    }

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar?) {
        controllers!!.add(controller)
    }

    override fun getAnimatableInstanceCache() = geoCache

    override fun registerGoals() {
        goalSelector.addGoal(0, FloatGoal(this))
        goalSelector.addGoal(1, HumifossorHideGoal(this))
        goalSelector.addGoal(2, WaterAvoidingRandomStrollGoal(this, 1.0))
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(STATE, 0)
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        state = State.entries[compound.getByte("state").toInt()]
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        compound.putByte("state", state.id)
    }
}