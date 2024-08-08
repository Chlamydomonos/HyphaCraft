package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.ModelProvider
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.DamageTypeLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class GrandisporiaWitheredStipeBlock : BaseHyphaBlock(Properties.ofFullCopy(Blocks.OAK_PLANKS)) {
    companion object {
        fun genModel(provider: ModBlockStateProvider) {
            provider.simpleBlockWithItem(
                BlockLoader.GRANDISPORIA_WITHERED_STIPE.block, provider.
                models().cubeColumn(
                    "grandisporia_withered_stipe",
                    NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/grandisporia_withered_stipe"),
                    NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/grandisporia_withered_stipe_top")
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