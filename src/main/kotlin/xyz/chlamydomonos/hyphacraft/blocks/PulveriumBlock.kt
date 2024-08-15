package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import xyz.chlamydomonos.hyphacraft.blockentities.PulveriumBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaEntityBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class PulveriumBlock : BaseHyphaEntityBlock(
    Properties.ofFullCopy(Blocks.OAK_PLANKS).randomTicks().noOcclusion().sound(SoundType.SLIME_BLOCK).instabreak()
) {
    companion object {
        val CODEC = simpleCodec { PulveriumBlock() }

        fun genModel(provider: ModBlockStateProvider) {
            val block = BlockLoader.PULVERIUM.block
            val name = NameUtil.path(block)
            val builder = provider.getVariantBuilder(block)
            for (i in 0..3) {
                builder.partialState().with(ModProperties.SPORE_AMOUNT, i).modelForState()
                    .modelFile(
                        provider.existingModel("${name}_${i}")
                    ).addModel()
            }
            provider.simpleBlockItem(block, provider.existingModel("${name}_0"))
        }

        val shapes = listOf(
            box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0),
            box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0),
            box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0),
            box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)
        )
    }

    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.SPORE_AMOUNT, 0))
    }

    override fun codec() = CODEC

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = PulveriumBlockEntity(pos, state)

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.SPORE_AMOUNT)
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val be = level.getBlockEntity(pos) as PulveriumBlockEntity
        if (be.blowing) {
            return
        }
        val sporeAmount = state.getValue(ModProperties.SPORE_AMOUNT)
        if (sporeAmount < 3) {
            level.setBlock(pos, state.cycle(ModProperties.SPORE_AMOUNT), 3)
        }
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return if (level.isClientSide) {
            null
        } else {
            BlockEntityTicker { _, _, _, e -> (e as PulveriumBlockEntity).tick() }
        }
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        val belowState = level.getBlockState(pos.below())
        return isFaceFull(belowState.getCollisionShape(level, pos.below()), Direction.UP)
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if(!canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState()
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos)
    }

    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val sporeAmount = state.getValue(ModProperties.SPORE_AMOUNT)
        if (sporeAmount > 0) {
            level.setBlock(pos, state.setValue(ModProperties.SPORE_AMOUNT, sporeAmount - 1), 3)
            level.scheduleTick(pos, this, 30)
        } else {
            val be = level.getBlockEntity(pos) as PulveriumBlockEntity
            be.blowing = false
        }
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return shapes[state.getValue(ModProperties.SPORE_AMOUNT)]
    }

    override fun onBurnt(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        replacing: Boolean,
        random: RandomSource
    ): BurnableHypha.VanillaBehaviourHandler {
        if (random.nextBoolean()) {
            level.setBlock(pos, BlockLoader.HYPHACOAL_BLOCK.block.defaultBlockState(), 3)
            return BurnableHypha.VanillaBehaviourHandler.CANCEL
        }
        return BurnableHypha.VanillaBehaviourHandler.DO
    }
}