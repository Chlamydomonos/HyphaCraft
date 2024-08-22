package xyz.chlamydomonos.hyphacraft.entity.goals

import net.minecraft.world.Difficulty
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.entity.player.Player
import xyz.chlamydomonos.hyphacraft.entity.entities.HumifossorEntity

class HumifossorAttackGoal(
    private val entity: HumifossorEntity
) : MeleeAttackGoal(entity, 3.0, false) {
    private var finishedAttack = false

    override fun canUse(): Boolean {
        if (entity.level().difficulty == Difficulty.PEACEFUL) {
            return false
        }

        if (!entity.charged) {
            return false
        }

        val target = entity.target ?: return false
        if (target is Player) {
            if (target.isCreative || target.isSpectator) {
                return false
            }
        }

        val result = super.canUse()
        if (result) {
            entity.attacking = true
            return true
        }
        return false
    }

    override fun start() {
        super.start()
        finishedAttack = false
    }

    override fun canContinueToUse(): Boolean {
        if (entity.level().difficulty == Difficulty.PEACEFUL) {
            return false
        }
        if (!entity.charged) {
            return false
        }
        val flag = super.canContinueToUse()
        finishedAttack = !flag
        return flag
    }

    override fun stop() {
        super.stop()
        entity.attacking = false
        if (finishedAttack) {
            entity.state = HumifossorEntity.Companion.State.EXPANDING
            entity.removeArmor()
            entity.coolDown = HumifossorHideGoal.ANIMATION_LEN
        }
    }
}