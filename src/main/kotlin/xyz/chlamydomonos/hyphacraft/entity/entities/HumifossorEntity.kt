package xyz.chlamydomonos.hyphacraft.entity.entities

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.level.Level
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.PlayState
import software.bernie.geckolib.animation.RawAnimation
import software.bernie.geckolib.util.GeckoLibUtil
import xyz.chlamydomonos.hyphacraft.entity.goals.HumifossorStrollGoal

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
    }

    private val geoCache = GeckoLibUtil.createInstanceCache(this)

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar?) {
        controllers!!.add(AnimationController(this, "moving", 2) {
            if (it.isMoving) it.setAndContinue(WALK_ANIM) else PlayState.STOP
        })
    }

    override fun getAnimatableInstanceCache() = geoCache

    override fun registerGoals() {
        goalSelector.addGoal(0, HumifossorStrollGoal(this))
    }
}