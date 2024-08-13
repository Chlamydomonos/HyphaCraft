package xyz.chlamydomonos.hyphacraft.effects

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3i
import xyz.chlamydomonos.hyphacraft.utils.CommonUtil
import kotlin.math.min

class CoveredWithSporeEffect : MobEffect(MobEffectCategory.NEUTRAL, 0x6fc9ef) {
    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean {
        return duration % 20 == 0
    }

    override fun applyEffectTick(livingEntity: LivingEntity, amplifier: Int): Boolean {
        if (livingEntity.level().isClientSide) {
            return true
        }

        val level = livingEntity.level() as ServerLevel
        val pos = BlockPos(livingEntity.position().toVec3i())
        val random = livingEntity.random
        val range = 1 shl min(amplifier, 3)
        for (i in -range..range) {
            for (j in -range..range) {
                for (k in -range..range) {
                    val newPos = pos.offset(i, j, k)
                    if (random.nextInt(10) == 0) {
                        CommonUtil.tryExpandHypha(level, newPos)
                    }
                }
            }
        }

        return true
    }
}