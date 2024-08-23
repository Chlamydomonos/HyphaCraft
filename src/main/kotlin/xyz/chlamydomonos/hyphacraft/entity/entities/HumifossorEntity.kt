package xyz.chlamydomonos.hyphacraft.entity.entities

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.RawAnimation
import software.bernie.geckolib.util.GeckoLibUtil
import xyz.chlamydomonos.hyphacraft.entity.entities.HumifossorEntity.Companion.State.*
import xyz.chlamydomonos.hyphacraft.entity.goals.HumifossorAttackGoal
import xyz.chlamydomonos.hyphacraft.entity.goals.HumifossorChargeGoal
import xyz.chlamydomonos.hyphacraft.entity.goals.HumifossorHideGoal
import xyz.chlamydomonos.hyphacraft.entity.goals.HumifossorPloughGoal
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.DamageTypeLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


class HumifossorEntity(
    entityType: EntityType<out PathfinderMob>,
    level: Level
) : PathfinderMob(entityType, level), GeoEntity {
    companion object {
        val BUILDER = EntityType.Builder.of(::HumifossorEntity, MobCategory.CREATURE)
        fun mobBuilder() = createMobAttributes()
            .add(Attributes.MAX_HEALTH, 10.0)
            .add(Attributes.MOVEMENT_SPEED, 0.2)
            .add(Attributes.ATTACK_DAMAGE, 10.0)

        val WALK_ANIM = RawAnimation.begin().thenLoop("walk")
        val CURL_ANIM = RawAnimation.begin().thenPlayAndHold("curl")
        val ROLL_ANIM = RawAnimation.begin().thenLoop("roll")
        val EXPAND_ANIM = RawAnimation.begin().thenPlayAndHold("expand")
        val IDLE_ANIM = RawAnimation.begin().thenLoop("idle")
        val HIDE_ANIM = RawAnimation.begin().thenLoop("hide")
        val PLOUGH_ANIM = RawAnimation.begin().thenLoop("plough")

        enum class State(val id: Byte) {
            BASE(0),
            HIDING(1),
            CURLING(2),
            EXPANDING(3),
            PLOUGHING(4)
        }

        private val STATE = SynchedEntityData.defineId(
            HumifossorEntity::class.java,
            EntityDataSerializers.BYTE
        )

        private val CHARGED = SynchedEntityData.defineId(
            HumifossorEntity::class.java,
            EntityDataSerializers.BOOLEAN
        )

        val ARMOR_MODIFIER_ID = NameUtil.getRL("humifossor_armor")
        val ARMOR_MODIFIER = AttributeModifier(ARMOR_MODIFIER_ID, 5.0, AttributeModifier.Operation.ADD_VALUE)
    }

    private val geoCache = GeckoLibUtil.createInstanceCache(this)

    var state
        get() = State.entries[entityData.get(STATE).toInt()]
        set(value) { entityData.set(STATE, value.id) }

    var charged
        get() = entityData.get(CHARGED)
        set(value) { entityData.set(CHARGED, value) }

    var coolDown = 0
    var attacking = false

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar?) {
        controllers!!.add(AnimationController(this, "moving", 0) {
            when (state) {
                BASE -> if (it.isMoving) it.setAndContinue(WALK_ANIM) else it.setAndContinue(IDLE_ANIM)
                HIDING -> if (it.isMoving) it.setAndContinue(ROLL_ANIM) else it.setAndContinue(HIDE_ANIM)
                CURLING -> it.setAndContinue(CURL_ANIM)
                EXPANDING -> it.setAndContinue(EXPAND_ANIM)
                PLOUGHING -> it.setAndContinue(PLOUGH_ANIM)
            }
        })
    }

    override fun getAnimatableInstanceCache() = geoCache

    override fun registerGoals() {
        goalSelector.addGoal(0, FloatGoal(this))
        goalSelector.addGoal(1, HumifossorAttackGoal(this))
        goalSelector.addGoal(2, HumifossorHideGoal(this))
        goalSelector.addGoal(3, HumifossorChargeGoal(this))
        goalSelector.addGoal(4, HumifossorPloughGoal(this))
        goalSelector.addGoal(5, WaterAvoidingRandomStrollGoal(this, 1.0))
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(STATE, 0)
        builder.define(CHARGED, false)
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        state = State.entries[compound.getByte("state").toInt()]
        charged = compound.getBoolean("charged")
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        compound.putByte("state", state.id)
        compound.putBoolean("charged", charged)
    }

    override fun tick() {
        super.tick()
        if (level().isClientSide) {
            return
        }

        if (coolDown == 0) {
            when (state) {
                CURLING -> {
                    state = HIDING
                    addArmor()
                }

                EXPANDING -> {
                    state = BASE
                }

                PLOUGHING -> {
                    state = BASE
                    val pos = blockPosition().below()
                    val state = level().getBlockState(pos)
                    if (state.`is`(BlockLoader.ALIEN_SOIL.block)) {
                        level().setBlock(pos, BlockLoader.ALIEN_SWARD.block.defaultBlockState(), 3)
                    } else if (state.`is`(BlockLoader.ALIEN_SWARD.block)) {
                        level().setBlock(pos, BlockLoader.FERTILE_ALIEN_SWARD.block.defaultBlockState(), 3)
                    }
                }

                else -> {}
            }
        } else {
            coolDown--
        }
    }

    override fun doHurtTarget(entity: Entity): Boolean {
        var f = getAttributeValue(Attributes.ATTACK_DAMAGE).toFloat()
        val damageSource = DamageSource(DamageTypeLoader.HYPHA_LIGHTNING(level()), this)
        val theLevel = level()
        if (theLevel is ServerLevel) {
            f = EnchantmentHelper.modifyDamage(theLevel, this.weaponItem, entity, damageSource, f)
        }

        val flag = entity.hurt(damageSource, f)
        if (flag) {
            val f1 = this.getKnockback(entity, damageSource)
            if (f1 > 0.0f && entity is LivingEntity) {
                entity.knockback(
                    f1 * 0.5,
                    sin(yRot * PI / 180.0),
                    -cos(yRot * PI / 180.0)
                )
                deltaMovement = deltaMovement.multiply(0.6, 1.0, 0.6)
            }

            if (theLevel is ServerLevel) {
                EnchantmentHelper.doPostAttackEffects(theLevel, entity, damageSource)
            }

            setLastHurtMob(entity)
            playAttackSound()
        }

        if (flag) {
            charged = false
        }

        return flag
    }

    private fun addArmor() {
        val attribute = getAttribute(Attributes.ARMOR)!!
        if (!attribute.hasModifier(ARMOR_MODIFIER_ID)) {
            attribute.addPermanentModifier(ARMOR_MODIFIER)
        }
    }

    fun removeArmor() {
        val attribute = getAttribute(Attributes.ARMOR)!!
        if (attribute.hasModifier(ARMOR_MODIFIER_ID)) {
            attribute.removeModifier(ARMOR_MODIFIER_ID)
        }
    }
}