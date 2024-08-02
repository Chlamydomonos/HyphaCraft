package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CarpetBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState

class SporeHeapBlock : CarpetBlock(
    Properties.ofFullCopy(Blocks.WHITE_CARPET).sound(SoundType.SNOW).randomTicks().noCollission()
) {
    override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
        if(level.isClientSide) {
            return
        }

        val effect = MobEffectInstance(MobEffects.POISON, 100, 0)
        if(entity is LivingEntity) {
            entity.addEffect(effect)
        }
    }
}