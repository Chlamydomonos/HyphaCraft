package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import xyz.chlamydomonos.hyphacraft.blockentities.FulgurfungusBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaEntityBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.BlockTagLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class FulgurfungusBlock : BaseHyphaEntityBlock(
    Properties.ofFullCopy(Blocks.DIRT)
        .randomTicks()
        .noOcclusion()
        .sound(SoundType.SLIME_BLOCK)
        .instabreak()
        .mapColor(MapColor.COLOR_LIGHT_BLUE)
        .ignitedByLava()
) {
    companion object {
        val CODEC = simpleCodec { FulgurfungusBlock() }

        fun genModel(provider: ModBlockStateProvider) {
            val block = BlockLoader.FULGURFUNGUS.block
            val name = NameUtil.path(block)
            val builder = provider.getVariantBuilder(block)
            val model0 = provider.existingModel("${name}_0")
            val model1 = provider.existingModel("${name}_1")

            builder.partialState().with(ModProperties.ACTIVE, false)
                .modelForState().modelFile(model0).addModel()
            builder.partialState().with(ModProperties.ACTIVE, true)
                .modelForState().modelFile(model1).addModel()
            provider.simpleBlockItem(block, model0)
        }

        private val STIPE = box(7.0, 0.0, 7.0, 9.0, 10.0, 9.0)
        private val CAP_0 = box(2.0, 9.0, 2.0, 14.0, 13.0, 14.0)
        private val CAP_1 = box(0.0, 9.0, 0.0, 16.0, 13.0, 16.0)
        val SHAPE_0 = Shapes.or(STIPE, CAP_0)
        val SHAPE_1 = Shapes.or(STIPE, CAP_1)
    }

    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.ACTIVE, false))
    }
    override fun codec() = CODEC

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = FulgurfungusBlockEntity(pos, state)

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.ACTIVE)
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (!state.getValue(ModProperties.ACTIVE)) {
            level.setBlock(pos, state.setValue(ModProperties.ACTIVE, true), 3)
        }
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        return level.getBlockState(pos.below()).`is`(BlockTagLoader.ALIEN_SOIL)
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return if (level.isClientSide) {
            null
        } else {
            BlockEntityTicker { _, _, _, e -> (e as FulgurfungusBlockEntity).tick() }
        }
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        val active = state.getValue(ModProperties.ACTIVE)
        return if (active) SHAPE_1 else SHAPE_0
    }
}