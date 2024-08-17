package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader


class GrandisporiaCapBlock : HorizontalDirectionalBlock(
    Properties.ofFullCopy(Blocks.DIRT)
        .sound(SoundType.SLIME_BLOCK)
        .noOcclusion()
        .mapColor(MapColor.COLOR_LIGHT_BLUE)
        .ignitedByLava()
), BurnableHypha {
    init {
        registerDefaultState(
            defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(ModProperties.IS_CORNER, false)
        )
    }

    companion object {
        val CODEC = simpleCodec { GrandisporiaCapBlock() }

        fun genModel(provider: ModBlockStateProvider) {
            val builder = provider.getVariantBuilder(BlockLoader.GRANDISPORIA_CAP)
            var dir = Direction.NORTH
            var rot = 0
            for (i in 1..4) {
                builder.partialState()
                    .with(FACING, dir)
                    .with(ModProperties.IS_CORNER, false)
                    .modelForState()
                    .modelFile(provider.existingModel("grandisporia_cap_side"))
                    .rotationY(rot)
                    .addModel()
                builder.partialState()
                    .with(FACING, dir)
                    .with(ModProperties.IS_CORNER, true)
                    .modelForState()
                    .modelFile(provider.existingModel("grandisporia_cap_corner"))
                    .rotationY(rot)
                    .addModel()
                dir = dir.clockWise
                rot += 90
            }
        }

        val SHAPES = mapOf(
            Pair(Direction.NORTH, box(2.0, 0.0, 2.0, 16.0, 12.0, 16.0)),
            Pair(Direction.EAST, box(0.0, 0.0, 2.0, 14.0, 12.0, 16.0)),
            Pair(Direction.SOUTH, box(0.0, 0.0, 0.0, 14.0, 12.0, 14.0)),
            Pair(Direction.WEST, box(2.0, 0.0, 0.0, 16.0, 12.0, 14.0))
        )
        val SIDE_SHAPE = box(0.0, 0.0 ,0.0, 16.0, 14.0, 16.0)
    }

    override fun codec() = CODEC

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING, ModProperties.IS_CORNER)
    }

    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5

    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        val isCorner = state.getValue(ModProperties.IS_CORNER)
        if (isCorner) {
            return SHAPES[state.getValue(FACING)]!!
        }
        return SIDE_SHAPE
    }
}