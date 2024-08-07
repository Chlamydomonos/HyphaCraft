package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import xyz.chlamydomonos.hyphacraft.blockentities.CarnivoravitisVineBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockTagLoader

class CarnivoravitisVineBlock : BaseEntityBlock(
    Properties.ofFullCopy(Blocks.OAK_PLANKS)
        .noOcclusion()
        .randomTicks()
        .sound(SoundType.SLIME_BLOCK)
) {
    init {
        registerDefaultState(
            defaultBlockState()
                .setValue(ModProperties.AGE, 0)
                .setValue(ModProperties.HEIGHT, 0)
                .setValue(BlockStateProperties.UP, false)
                .setValue(BlockStateProperties.DOWN, false)
                .setValue(BlockStateProperties.NORTH, false)
                .setValue(BlockStateProperties.SOUTH, false)
                .setValue(BlockStateProperties.EAST, false)
                .setValue(BlockStateProperties.WEST, false)
        )
    }

    companion object {
        val CODEC = simpleCodec { CarnivoravitisVineBlock() }
    }

    override fun codec() = CODEC

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(
            ModProperties.AGE,
            ModProperties.HEIGHT,
            BlockStateProperties.UP,
            BlockStateProperties.DOWN,
            BlockStateProperties.NORTH,
            BlockStateProperties.SOUTH,
            BlockStateProperties.EAST,
            BlockStateProperties.WEST
        )
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = CarnivoravitisVineBlockEntity(pos, state)

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.MODEL
    }


    private fun canConnect(state: BlockState, direction: Direction): Boolean {
        return state.`is`(BlockTagLoader.CARNIVORAVITIS_VINE_CONNECTABLE)
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
            return state.setValue(PipeBlock.PROPERTY_BY_DIRECTION[direction]!!, !neighborState.isEmpty)
        }

        return state.setValue(PipeBlock.PROPERTY_BY_DIRECTION[direction]!!, canConnect(neighborState, direction))
    }
}