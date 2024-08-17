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
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ModelProvider
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil
import xyz.chlamydomonos.hyphacraft.utils.plant.TerraborerUtil

class TerraborerBombBlock : BaseHyphaBlock(
    Properties.ofFullCopy(Blocks.DIRT)
        .randomTicks()
        .instabreak()
        .noOcclusion()
        .mapColor(MapColor.COLOR_BLACK)
        .ignitedByLava()
) {
    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.CONTAINS_WATER, false))
    }

    companion object {
        fun genModel(provider: ModBlockStateProvider) {
            val builder = provider.getVariantBuilder(BlockLoader.TERRABORER_BOMB.block)
            val name = NameUtil.path(BlockLoader.TERRABORER_BOMB.block)
            builder.partialState().with(ModProperties.CONTAINS_WATER, true)
                .setModels(ConfiguredModel(
                    provider.models().cubeAll(
                        "${name}_1",
                        NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/${name}_1")
                    )
                ))
            builder.partialState().with(ModProperties.CONTAINS_WATER, false)
                .setModels(ConfiguredModel(provider.existingModel("${name}_0")))
            provider.simpleBlockItem(BlockLoader.TERRABORER_BOMB.block, provider.existingModel("${name}_0"))
        }

        val SHAPE_0 = box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0)
        val SHAPE_1 = box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.CONTAINS_WATER)
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val containsWater = state.getValue(ModProperties.CONTAINS_WATER)
        if(containsWater) {
            TerraborerUtil.explode(level, pos, random)
            return
        }

        if (level.isRaining) {
            level.setBlock(pos, state.setValue(ModProperties.CONTAINS_WATER, true), 3)
        }
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        val belowState = level.getBlockState(pos.below())
        return belowState.`is`(BlockLoader.TERRABORER_STIPE.block) ||
                isFaceFull(belowState.getCollisionShape(level, pos.below()), Direction.UP)
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (!canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState()
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        val containsWater = state.getValue(ModProperties.CONTAINS_WATER)
        return if (containsWater) SHAPE_1 else SHAPE_0
    }

    override fun destroy(level: LevelAccessor, pos: BlockPos, state: BlockState) {
        val containsWater = state.getValue(ModProperties.CONTAINS_WATER)
        if (containsWater && !level.isClientSide) {
           TerraborerUtil.explode(level as ServerLevel, pos, level.random)
        }
    }

    override fun onBurnt(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        replacing: Boolean,
        random: RandomSource
    ): BurnableHypha.VanillaBehaviourHandler {
        if (!level.isClientSide) {
            level.setBlock(pos, BlockLoader.HYPHACOAL_BLOCK.block.defaultBlockState(), 3)
        }
        return BurnableHypha.VanillaBehaviourHandler.CANCEL
    }
}