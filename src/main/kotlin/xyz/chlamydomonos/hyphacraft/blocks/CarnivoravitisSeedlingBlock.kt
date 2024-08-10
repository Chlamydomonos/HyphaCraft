package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.neoforged.neoforge.client.model.generators.ModelProvider
import xyz.chlamydomonos.hyphacraft.blockentities.CarnivoravitisVineBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.base.ImmuneToHyphaExplosionBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class CarnivoravitisSeedlingBlock : ImmuneToHyphaExplosionBlock(
    Properties.ofFullCopy(Blocks.DIRT)
        .noOcclusion()
        .randomTicks()
        .instabreak()
        .noCollission()
        .noLootTable()
        .sound(SoundType.SLIME_BLOCK)
) {
    companion object {
        fun genModel(provider: ModBlockStateProvider) {
            val name = NameUtil.path(BlockLoader.CARNIVORAVITIS_SEEDLING)
            val model = provider.models()
                .cross(name, NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/$name"))
                .renderType("cutout")
            provider.simpleBlock(BlockLoader.CARNIVORAVITIS_SEEDLING, model)
        }
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        return level.getBlockState(pos.below()).`is`(BlockLoader.CARNIVORAVITIS_ROOT)
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val belowState = level.getBlockState(pos.below())
        if (!belowState.`is`(BlockLoader.CARNIVORAVITIS_ROOT)) {
            return
        }
        val age = belowState.getValue(ModProperties.AGE)
        if (age == 3) {
            var newState = BlockLoader.CARNIVORAVITIS_VINE.defaultBlockState()
            newState = newState.setValue(BlockStateProperties.DOWN, true)
            level.setBlock(pos, newState, 3)
            val be = level.getBlockEntity(pos) as CarnivoravitisVineBlockEntity
            be.nextPos = pos.offset(0, -3, 0)
        }
    }
}