package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.neoforged.neoforge.client.model.generators.ModelProvider
import xyz.chlamydomonos.hyphacraft.blocks.base.ImmuneToHyphaExplosionBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil
import xyz.chlamydomonos.hyphacraft.utils.plant.CarnivoravitisUtil

class CarnivoravitisRootBlock : ImmuneToHyphaExplosionBlock(
    Properties.ofFullCopy(Blocks.OAK_PLANKS)
        .sound(SoundType.FUNGUS)
        .randomTicks()
) {
    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.AGE, 3))
    }

    companion object {
        fun genModel(provider: ModBlockStateProvider) {
            provider.simpleBlockWithItem(
                BlockLoader.CARNIVORAVITIS_ROOT,
                provider.models().cubeColumn(
                    "carnivoravitis_root",
                    NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/carnivoravitis_shell"),
                    NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/carnivoravitis_root")
                )
            )
        }
    }

    override fun onBurnt(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        replacing: Boolean,
        random: RandomSource
    ): BurnableHypha.VanillaBehaviourHandler {
        level.setBlock(pos, BlockLoader.HYPHACOAL_BLOCK.block.defaultBlockState(), 3)
        return BurnableHypha.VanillaBehaviourHandler.CANCEL
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.AGE)
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val age = state.getValue(ModProperties.AGE)
        if (age == 1) {
            if (!CarnivoravitisUtil.canGrowRoot(level, pos)) {
                level.setBlock(pos, BlockLoader.ACTIVE_HYPHA_BLOCK.block.defaultBlockState(), 3)
                return
            } else {
                CarnivoravitisUtil.growRoot(level, pos)
            }
        }
        if (age < 3) {
            level.setBlock(pos, state.cycle(ModProperties.AGE), 3)
        }
    }
}