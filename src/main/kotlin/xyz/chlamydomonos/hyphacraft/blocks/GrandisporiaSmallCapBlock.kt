package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class GrandisporiaSmallCapBlock : BaseHyphaBlock(
    Properties.ofFullCopy(Blocks.DIRT)
        .noOcclusion()
        .sound(SoundType.SLIME_BLOCK)
        .randomTicks()
) {
    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.CAN_GROW, false))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.CAN_GROW)
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val canGrow = state.getValue(ModProperties.CAN_GROW)
        if (!canGrow) return
        val downState = level.getBlockState(pos.below())
        if (downState.block !== BlockLoader.GRANDISPORIA_STIPE) return

        if (downState.getValue(ModProperties.AGE) < 2) return

        val centerState = BlockLoader.GRANDISPORIA_CAP_CENTER.defaultBlockState()
        val northState = BlockLoader.GRANDISPORIA_CAP.defaultBlockState()
        val northWestState = northState.setValue(ModProperties.IS_CORNER, true)

        val facing = HorizontalDirectionalBlock.FACING

        level.setBlock(pos, centerState, 3)
        if (level.getBlockState(pos.offset(0, 0, -1)).isEmpty) {
            level.setBlock(pos.offset(0, 0, -1), northState, 3)
        }
        if (level.getBlockState(pos.offset(-1, 0, -1)).isEmpty) {
            level.setBlock(pos.offset(-1, 0, -1), northWestState, 3)
        }
        if (level.getBlockState(pos.offset(-1, 0, 0)).isEmpty) {
            level.setBlock(pos.offset(-1, 0, 0), northState.setValue(facing, Direction.WEST), 3)
        }
        if (level.getBlockState(pos.offset(-1, 0, 1)).isEmpty) {
            level.setBlock(pos.offset(-1, 0, 1), northWestState.setValue(facing, Direction.WEST), 3)
        }
        if (level.getBlockState(pos.offset(0, 0, 1)).isEmpty) {
            level.setBlock(pos.offset(0, 0, 1), northState.setValue(facing, Direction.SOUTH), 3)
        }
        if (level.getBlockState(pos.offset(1, 0, 1)).isEmpty) {
            level.setBlock(pos.offset(1, 0, 1), northWestState.setValue(facing, Direction.SOUTH), 3)
        }
        if (level.getBlockState(pos.offset(1, 0, 0)).isEmpty) {
            level.setBlock(pos.offset(1, 0, 0), northState.setValue(facing, Direction.EAST), 3)
        }
        if (level.getBlockState(pos.offset(1, 0, -1)).isEmpty) {
            level.setBlock(pos.offset(1, 0, -1), northWestState.setValue(facing, Direction.EAST), 3)
        }
    }
}