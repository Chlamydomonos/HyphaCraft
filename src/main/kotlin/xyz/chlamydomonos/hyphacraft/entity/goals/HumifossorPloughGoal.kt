package xyz.chlamydomonos.hyphacraft.entity.goals

import net.minecraft.world.entity.ai.goal.Goal
import xyz.chlamydomonos.hyphacraft.entity.entities.HumifossorEntity
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import java.util.*

class HumifossorPloughGoal(val entity: HumifossorEntity) : Goal() {
    companion object {
        const val ANIMATION_LENGTH = 45
    }

    init {
        flags = EnumSet.of(Flag.MOVE)
    }

    override fun canUse(): Boolean {
        if (entity.random.nextInt(300) != 0) {
            return false
        }
        val state = entity.level().getBlockState(entity.blockPosition().below())
        return state.`is`(BlockLoader.ALIEN_SOIL.block) || state.`is`(BlockLoader.ALIEN_SWARD.block)
    }

    override fun start() {
        entity.state = HumifossorEntity.Companion.State.PLOUGHING
        entity.coolDown = ANIMATION_LENGTH
    }

    override fun canContinueToUse(): Boolean {
        return entity.state == HumifossorEntity.Companion.State.PLOUGHING && entity.coolDown > 0
    }
}