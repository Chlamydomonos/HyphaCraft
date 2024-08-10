package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder
import xyz.chlamydomonos.hyphacraft.blockentities.CarnivoravitisFlowerBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaEntityBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.DamageTypeLoader

class CarnivoravitisFlowerBlock : BaseHyphaEntityBlock(
    Properties.ofFullCopy(Blocks.DIRT)
        .sound(SoundType.SLIME_BLOCK)
        .noOcclusion()
        .instabreak()
) {
    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.DIRECTION, Direction.UP))
    }

    companion object {
        val CODEC = simpleCodec { CarnivoravitisFlowerBlock() }

        private fun genModelWithParams(
            provider: ModBlockStateProvider,
            builder: VariantBlockStateBuilder,
            direction: Direction,
            rx: Int,
            ry: Int
        ) {
            builder.partialState().with(ModProperties.DIRECTION, direction).modelForState()
                .modelFile(provider.existingModel("carnivoravitis_flower"))
                .rotationX(rx)
                .rotationY(ry)
                .addModel()
        }

        fun genModel(provider: ModBlockStateProvider) {
            val builder = provider.getVariantBuilder(BlockLoader.CARNIVORAVITIS_FLOWER)
            genModelWithParams(provider, builder, Direction.UP, 0, 0)
            genModelWithParams(provider, builder, Direction.DOWN, 180, 0)
            var dir = Direction.NORTH
            var ry = 0
            for (i in 1..4) {
                genModelWithParams(provider, builder, dir, 90, ry)
                dir = dir.clockWise
                ry += 90
            }
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.DIRECTION)
    }

    override fun codec() = CODEC

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = CarnivoravitisFlowerBlockEntity(pos, state)

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return if (level.isClientSide) {
            null
        } else {
            BlockEntityTicker { _, _, _, e -> (e as CarnivoravitisFlowerBlockEntity).tick() }
        }
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