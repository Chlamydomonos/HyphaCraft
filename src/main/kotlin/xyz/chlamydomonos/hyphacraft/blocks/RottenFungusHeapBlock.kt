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

class RottenFungusHeapBlock : CarpetBlock(
    Properties.ofFullCopy(Blocks.BLUE_CARPET).sound(SoundType.SLIME_BLOCK).noCollission()
) {
    override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
        if(level.isClientSide) {
            return
        }

        val effect1 = MobEffectInstance(MobEffects.POISON, 200, 0)
        val effect2 = MobEffectInstance(MobEffects.CONFUSION, 200, 0)
        if(entity is LivingEntity) {
            entity.addEffect(effect1)
            entity.addEffect(effect2)
        }
    }
}