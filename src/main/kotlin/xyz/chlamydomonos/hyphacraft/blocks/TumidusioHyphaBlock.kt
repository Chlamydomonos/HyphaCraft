package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import xyz.chlamydomonos.hyphacraft.blockentities.TumidusioHyphaBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.plant.TumidusioUtil

class TumidusioHyphaBlock : BaseEntityBlock(
    Properties.ofFullCopy(Blocks.DIRT)
        .sound(SoundType.SLIME_BLOCK)
        .ignitedByLava()
        .randomTicks()
), BurnableHypha {
    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.PHASE, 0))
    }

    companion object {
        val CODEC = simpleCodec { TumidusioHyphaBlock() }
    }

    override fun codec() = CODEC

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = TumidusioHyphaBlockEntity(pos, state)

    override fun onBurnt(state: BlockState, level: Level, pos: BlockPos, replacing: Boolean, random: RandomSource): BurnableHypha.VanillaBehaviourHandler {
        val phase = state.getValue(ModProperties.PHASE)
        if (phase < 10) {
            return BurnableHypha.VanillaBehaviourHandler.DO
        }

        level.setBlock(pos, BlockLoader.HYPHACOAL_BLOCK.block.defaultBlockState(), 3)
        return BurnableHypha.VanillaBehaviourHandler.CANCEL
    }

    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 20

    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 100

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.PHASE)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val phase = state.getValue(ModProperties.PHASE)
        for (i in -1..1) {
            for(j in -1..1) {
                for(k in -1..1) {
                    val newPos = pos.offset(i, j, k)
                    if(random.nextFloat() < TumidusioUtil.EXPAND_RATE && TumidusioUtil.canHyphaGrow(level, newPos)) {
                        TumidusioUtil.setHypha(level, newPos)
                    }
                }
            }
        }

        if(phase < 14) {
            TumidusioUtil.setHypha(level, pos, phase + 1)
        } else {
            level.setBlock(
                pos,
                BlockLoader.TUMIDUSIO.block.defaultBlockState().setValue(ModProperties.DENSITY, 3),
                3
            )
            level.scheduleTick(pos, BlockLoader.TUMIDUSIO.block, 1)
        }
    }
}