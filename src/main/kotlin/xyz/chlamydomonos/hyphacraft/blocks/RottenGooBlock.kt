package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.MapColor
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.entity.ModEntityTags
import xyz.chlamydomonos.hyphacraft.loaders.FluidLoader

class RottenGooBlock : LiquidBlock(
    FluidLoader.ROTTEN_GOO.source,
    Properties.ofFullCopy(Blocks.WATER).mapColor(MapColor.TERRACOTTA_GREEN).ignitedByLava()
), BurnableHypha {
    override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
        if (level.isClientSide) {
            return
        }

        if (entity is LivingEntity && entity.getFluidTypeHeight(FluidLoader.ROTTEN_GOO.type) > 0.0) {
            entity.addEffect(MobEffectInstance(MobEffects.CONFUSION, 300 ,0))
            if (!entity.type.`is`(ModEntityTags.HYPHACRAFT_INSECT)) {
                entity.addEffect(MobEffectInstance(MobEffects.POISON, 300, 2))
            }
        }
    }

    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5

    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5
}