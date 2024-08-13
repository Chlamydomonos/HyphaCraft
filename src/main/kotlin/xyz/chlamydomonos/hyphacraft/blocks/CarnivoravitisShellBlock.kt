package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import xyz.chlamydomonos.hyphacraft.blocks.base.ImmuneToHyphaExplosionBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class CarnivoravitisShellBlock : ImmuneToHyphaExplosionBlock(
    Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.FUNGUS).randomTicks()
) {
    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.DIRECTION, Direction.DOWN))
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (state.getValue(ModProperties.DIRECTION) != Direction.UP) {
            return
        }

        var newPos = pos.above()
        var isEmpty = level.getBlockState(newPos).isEmpty
        var isShell: Boolean
        if (!isEmpty) {
            return
        }

        var canGenFluid = false
        for (i in 2..6) {
            newPos = newPos.above()
            val newState = level.getBlockState(newPos)
            isEmpty = newState.isEmpty
            isShell = newState.`is`(this)
            if (isShell) {
                canGenFluid = true
                break
            } else if(!isEmpty) {
                return
            }
        }

        if (canGenFluid) {
            level.setBlock(pos.above(), BlockLoader.DIGESTIVE_JUICE_BLOCK.defaultBlockState(), 3)
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.DIRECTION)
    }

    override fun onBurnt(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        replacing: Boolean,
        random: RandomSource
    ): BurnableHypha.VanillaBehaviourHandler {
        level.setBlock(pos, BlockLoader.HYPHACOAL_BLOCK.block.defaultBlockState(), 3)
        return BurnableHypha.VanillaBehaviourHandler.CANCEL
    }
}