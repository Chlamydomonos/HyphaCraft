package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CarpetBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.blocks.base.BurnableHypha

class HumusHeapBlock : CarpetBlock(
    Properties.ofFullCopy(Blocks.CYAN_CARPET).sound(SoundType.SLIME_BLOCK).randomTicks().noCollission()
), BurnableHypha {
    override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
        if(level.isClientSide) {
            return
        }

        val effect1 = MobEffectInstance(MobEffects.POISON, 100, 0)
        val effect2 = MobEffectInstance(MobEffects.CONFUSION, 300, 0)
        if(entity is LivingEntity) {
            entity.addEffect(effect1)
            entity.addEffect(effect2)
        }
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        val belowState = level.getBlockState(pos.below())
        return isFaceFull(belowState.getCollisionShape(level, pos.below()), Direction.UP)
    }

    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5

    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5
}