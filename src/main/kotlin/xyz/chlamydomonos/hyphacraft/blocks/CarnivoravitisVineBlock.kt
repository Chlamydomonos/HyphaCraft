package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.PipeBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder
import xyz.chlamydomonos.hyphacraft.blockentities.CarnivoravitisVineBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaEntityBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.BlockTagLoader
import xyz.chlamydomonos.hyphacraft.loaders.DamageTypeLoader
import xyz.chlamydomonos.hyphacraft.utils.plant.CarnivoravitisUtil

class CarnivoravitisVineBlock : BaseHyphaEntityBlock(
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

        private fun genModelWithParams(
            provider: ModBlockStateProvider,
            builder: MultiPartBlockStateBuilder,
            property: BooleanProperty,
            rx: Int,
            ry: Int
        ) {
            builder.part()
                .modelFile(provider.existingModel("carnivoravitis_vine"))
                .rotationX(rx)
                .rotationY(ry)
                .addModel()
                .condition(property, false)
        }

        fun genModel(provider: ModBlockStateProvider) {
            val builder = provider.getMultipartBuilder(BlockLoader.CARNIVORAVITIS_VINE)
            genModelWithParams(provider, builder, BlockStateProperties.UP, 270, 0)
            genModelWithParams(provider, builder, BlockStateProperties.DOWN, 90, 0)
            var dir = Direction.NORTH
            var ry = 0
            for (i in 1..4) {
                genModelWithParams(provider, builder, PipeBlock.PROPERTY_BY_DIRECTION[dir]!!, 0, ry)
                dir = dir.clockWise
                ry += 90
            }
        }
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

    private fun canConnect(state: BlockState, direction: Direction): Boolean {
        if (state.`is`(BlockLoader.CARNIVORAVITIS_FLOWER)) {
            val neighborDirection = state.getValue(ModProperties.DIRECTION)
            return neighborDirection == direction.opposite
        }

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

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        CarnivoravitisUtil.growVine(level, pos, state, random)
    }

    override fun onBlockExploded(state: BlockState, level: Level, pos: BlockPos, explosion: Explosion) {
        if (level.isClientSide) {
            return
        }
        val damageType = explosion.damageSource.type()
        if (damageType != DamageTypeLoader.HYPHA_EXPLOSION(level).value()) {
            super.onBlockExploded(state, level, pos, explosion)
        }
    }
}