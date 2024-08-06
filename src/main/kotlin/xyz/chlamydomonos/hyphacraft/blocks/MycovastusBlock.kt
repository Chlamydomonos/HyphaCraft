package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.plant.MycovastusUtil

class MycovastusBlock : Block(
    Properties.ofFullCopy(Blocks.MUSHROOM_STEM)
        .instabreak()
        .noOcclusion()
        .randomTicks()
        .sound(SoundType.SLIME_BLOCK)
), SimpleWaterloggedBlock, BurnableHypha {
    companion object {
        val SHAPE_1 = box(5.0, 0.0, 5.0, 11.0, 2.0, 11.0)
        val SHAPE_2 = box(1.0, 0.0, 1.0, 15.0, 2.0, 13.0)
        val SHAPE_3 = box(0.0, 0.0, 0.0, 14.0, 2.0, 14.0)
    }

    init {
        registerDefaultState(
            defaultBlockState()
                .setValue(ModProperties.MUSHROOM_COUNT, 1)
                .setValue(BlockStateProperties.WATERLOGGED, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.MUSHROOM_COUNT, BlockStateProperties.WATERLOGGED)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        val oldState = context.level.getBlockState(context.clickedPos)
        if(oldState.`is`(this)) {
            return oldState.cycle(ModProperties.MUSHROOM_COUNT)
        } else {
            val fluidState = context.level.getFluidState(context.clickedPos)
            val flag = fluidState.type == Fluids.WATER
            return super.getStateForPlacement(context)!!.setValue(BlockStateProperties.WATERLOGGED, flag)
        }
    }

    override fun canBeReplaced(state: BlockState, useContext: BlockPlaceContext): Boolean {
        return if (
            !useContext.isSecondaryUseActive
            && useContext.itemInHand.item === asItem()
            && state.getValue(ModProperties.MUSHROOM_COUNT) < 3
        ) {
            true
        } else {
            super.canBeReplaced(state, useContext)
        }
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if(state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level))
        }

        if(!canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState()
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        val count = state.getValue(ModProperties.MUSHROOM_COUNT)
        return when (count) {
            1 -> {
                SHAPE_1
            }
            2 -> {
                SHAPE_2
            }
            else -> {
                SHAPE_3
            }
        }
    }

    override fun getFluidState(state: BlockState) =
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            Fluids.WATER.getSource(false)
        } else {
            super.getFluidState(state)
        }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        val belowState = level.getBlockState(pos.below())
        if (belowState.`is`(BlockLoader.MYCOVASTUS_HYPHA)) {
            return true
        }
        return MycovastusUtil.canHyphaGrow(level, pos.below())
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if(random.nextFloat() < MycovastusUtil.EXPAND_RATE) {
            val belowPos = pos.below()
            val belowState = level.getBlockState(belowPos)
            if (!belowState.`is`(BlockLoader.MYCOVASTUS_HYPHA) && MycovastusUtil.canHyphaGrow(level, belowPos)) {
                MycovastusUtil.setHypha(level, belowPos)
            }
        }
    }

    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5

    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5
}