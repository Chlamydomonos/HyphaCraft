package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.MapColor
import net.neoforged.neoforge.client.model.generators.ModelProvider
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil


class GrandisporiaCapCenterBlock : BaseHyphaBlock(
    Properties.ofFullCopy(Blocks.DIRT)
        .sound(SoundType.SLIME_BLOCK)
        .randomTicks()
        .mapColor(MapColor.COLOR_LIGHT_BLUE)
        .ignitedByLava()
) {
    companion object {
        fun genModel(provider: ModBlockStateProvider) {
            val thisBlock = BlockLoader.GRANDISPORIA_CAP_CENTER
            provider.simpleBlock(thisBlock, provider.models().cubeBottomTop(
                NameUtil.path(thisBlock),
                NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/grandisporia_cap_inner"),
                NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/grandisporia_cap_center_bottom"),
                NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/grandisporia_cap")
            ))
        }
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val downState = level.getBlockState(pos.below())
        if(!downState.`is`(BlockLoader.GRANDISPORIA_WITHERED_STIPE.block)) {
            return
        }

        val canGrowBuffer = Array(5) { BooleanArray(5) }
        for (x in -1..1) {
            for (z in -1..1) {
                if (level.getBlockState(pos.offset(x, 0, z)).`is`(BlockLoader.GRANDISPORIA_CAP)) {
                    canGrowBuffer[x + 2][z + 2] = true
                }
                for (i in -1..1) {
                    for (k in -1..1) {
                        if(level.getBlockState(pos.offset(x + i, 0, z + k)).isEmpty) {
                            canGrowBuffer[x + i + 2][z + k + 2] = true
                        }
                    }
                }
            }
        }

        for (i in 0..4) {
            for (k in 0..4) {
                if ((i == 0 || i == 4) && (k == 0 || k == 4)) continue

                if (canGrowBuffer[i][k]) {
                    val newPos = pos.offset(i - 2, 0, k - 2)
                    val newState = BlockLoader.GRANDISPORIA_WITHERED_CAP.block.defaultBlockState()
                        .setValue(ModProperties.SPORE_AMOUNT, 3)
                    level.setBlock(newPos, newState, 3)
                }
            }
        }

        level.setBlock(pos, BlockLoader.GRANDISPORIA_WITHERED_CAP.block.defaultBlockState(), 3)
    }
}