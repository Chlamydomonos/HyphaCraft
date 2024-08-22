package xyz.chlamydomonos.hyphacraft.entity.goals

import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.player.Player
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.minus
import xyz.chlamydomonos.hyphacraft.entity.entities.HumifossorEntity
import java.util.*
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

class HumifossorHideGoal(
    val entity: HumifossorEntity
) : Goal() {
    companion object {
        const val ANIMATION_LEN = 24
    }

    init {
        flags = EnumSet.of(Flag.MOVE)
    }

    private var player: Player? = null

    override fun canUse(): Boolean {
        if (entity.coolDown > 0 && entity.state != HumifossorEntity.Companion.State.PLOUGHING) {
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

        if (thePlayer.isCrouching) {
            if (entity.distanceToSqr(thePlayer) < 1) {
                return true
            }

            val yRot = entity.yRot / 180 * PI
            val relative = thePlayer.position() - entity.position()
            val angleToZ = atan2(relative.x, relative.z)
            val angleToPlayer = abs(angleToZ - yRot)
            if (angleToPlayer > PI / 4) {
                return false
            }
        }

        return true
    }

    override fun start() {
        if (entity.state != HumifossorEntity.Companion.State.HIDING) {
            entity.target = player
            entity.state = HumifossorEntity.Companion.State.CURLING
            entity.coolDown = ANIMATION_LEN
        }
    }

    override fun canContinueToUse(): Boolean {
        val thePlayer = entity.target
        if (thePlayer == null || !thePlayer.isAlive) {
            return false
        }
        if (entity.distanceToSqr(thePlayer) > 25) {
            return false
        }
        return true
    }

    override fun stop() {
        if (!entity.attacking) {
            entity.target = null
            entity.state = HumifossorEntity.Companion.State.EXPANDING
            entity.removeArmor()
            entity.coolDown = ANIMATION_LEN
        }
    }
}