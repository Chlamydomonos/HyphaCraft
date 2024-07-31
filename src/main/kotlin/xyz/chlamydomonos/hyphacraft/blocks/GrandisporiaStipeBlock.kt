package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.PipeBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class GrandisporiaStipeBlock : PipeBlock(
    0.5f,
    Properties.ofFullCopy(Blocks.OAK_LOG)
        .noOcclusion()
        .randomTicks()
        .sound(SoundType.FUNGUS)
) {
    init {
        registerDefaultState(
            defaultBlockState()
                .setValue(ModProperties.AGE, 0)
                .setValue(ModProperties.HEIGHT, 0)
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
        )
    }

    companion object {
        val CODEC = simpleCodec { GrandisporiaStipeBlock() }
    }

    override fun codec() = CODEC

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.AGE, ModProperties.HEIGHT, UP, DOWN, NORTH, SOUTH, EAST, WEST)
    }

    private fun canConnect(state: BlockState, direction: Direction): Boolean {
        if(state.`is`(this)) {
            return true
        }
        if(state.`is`(BlockLoader.GRANDISPORIA_SMALL_CAP) && direction == Direction.UP) {
            return true
        }
        if(state.`is`(BlockLoader.GRANDISPORIA_CAP_CENTER)) {
            return true
        }
        return false
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if(state.getValue(ModProperties.HEIGHT) == 0 && direction == Direction.DOWN) {
            return state.setValue(PROPERTY_BY_DIRECTION[direction]!!, !neighborState.isEmpty)
        }

        return state.setValue(PROPERTY_BY_DIRECTION[direction]!!, canConnect(neighborState, direction))
    }
}