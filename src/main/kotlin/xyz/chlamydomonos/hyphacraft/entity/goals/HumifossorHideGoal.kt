package xyz.chlamydomonos.hyphacraft.entity.goals

import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.player.Player
import xyz.chlamydomonos.hyphacraft.entity.entities.HumifossorEntity
import java.util.*

class HumifossorHideGoal(
    val entity: HumifossorEntity
) : Goal() {
    companion object {
        const val ANIMATION_LEN = 12
    }

    init {
        flags = EnumSet.of(Goal.Flag.MOVE)
    }

    private var player: Player? = null
    private var coolDown = 0
    private var stopping = true

    override fun canUse(): Boolean {
        if (coolDown > 0) {
            return false
        }

        player = entity.level().getNearestPlayer(entity, 5.0)
        if (player == null) {
            return false
        }
        val thePlayer = player!!
        if (thePlayer.isSpectator) {
            return false
        }
        return true
    }

    override fun start() {
        super.start()
        coolDown = ANIMATION_LEN
        entity.state = HumifossorEntity.Companion.State.CURLING
    }

    override fun tick() {
        super.tick()
        if (stopping && coolDown == 0) {
            entity.state = HumifossorEntity.Companion.State.EXPANDING
            coolDown = ANIMATION_LEN
        } else if (coolDown > 0) {
            coolDown--
        } else if (coolDown == 0) {
            entity.state = HumifossorEntity.Companion.State.HIDING
        }
    }

    override fun canContinueToUse(): Boolean {
        if (coolDown > 0) {
            return true
        }

        if (player == null || !player!!.isAlive) {
            return false
        }

        val thePlayer = player!!
        if (thePlayer.isSpectator) {
            return false
        }

        if (entity.distanceToSqr(thePlayer) > 25) {
            if (stopping) {
                stopping = false
                return false
            }
            stopping = true
            return true
        }

        return true
    }
}