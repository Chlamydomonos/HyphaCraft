package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.PipeBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.plant.GrandisporiaUtil

class GrandisporiaStipeBlock : PipeBlock(
    0.5f,
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

        private fun genModelWithParams(
            provider: ModBlockStateProvider,
            builder: MultiPartBlockStateBuilder,
            directionProperty: BooleanProperty,
            trueValue: String,
            falseValue: String,
            tRX: Int = 0,
            tRY: Int = 0,
            fRX: Int = 0,
            fRY: Int = 0
        ) {
            for (phase in 0..1) {
                builder.part()
                    .modelFile(provider.existingModel("grandisporia_stipe_${trueValue}_$phase"))
                    .rotationX(tRX)
                    .rotationY(tRY)
                    .addModel()
                    .condition(directionProperty, true)
                    .condition(ModProperties.AGE, phase * 2, phase * 2 + 1)
                builder.part()
                    .modelFile(provider.existingModel("grandisporia_stipe_${falseValue}_${phase}"))
                    .rotationX(fRX)
                    .rotationY(fRY)
                    .addModel()
                    .condition(directionProperty, false)
                    .condition(ModProperties.AGE, phase * 2, phase * 2 + 1)
            }
        }

        fun genModel(provider: ModBlockStateProvider) {
            val builder = provider.getMultipartBuilder(BlockLoader.GRANDISPORIA_STIPE)
            genModelWithParams(provider, builder, UP, "connection", "top")
            genModelWithParams(provider, builder, DOWN, "connection", "side", tRX = 180, fRX = 90)
            var dir = Direction.NORTH
            val tRX = 90
            var ry = 0
            for(i in 1..4) {
                genModelWithParams(
                    provider,
                    builder,
                    PROPERTY_BY_DIRECTION[dir]!!,
                    "connection",
                    "side",
                    tRX = tRX,
                    tRY = ry,
                    fRY = ry
                )

                dir = dir.clockWise
                ry += 90
            }
        }
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
        if(state.`is`(BlockLoader.GRANDISPORIA_WITHERED_STIPE.block)) {
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

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        GrandisporiaUtil.grow(state, level, pos, random)
    }
}