package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.AlienSoilUtil
import xyz.chlamydomonos.hyphacraft.utils.CommonUtil
import xyz.chlamydomonos.hyphacraft.utils.plant.GrandisporiaUtil
import xyz.chlamydomonos.hyphacraft.utils.plant.TerraborerUtil

class AlienSoilBlock : BaseHyphaBlock(Properties.ofFullCopy(Blocks.DIRT).randomTicks()) {
    companion object {
        const val EXPAND_RATE = 1.0f / 20.0f
    }

    override fun onBurnt(state: BlockState, level: Level, pos: BlockPos, replacing: Boolean, random: RandomSource): BurnableHypha.VanillaBehaviourHandler {
        level.setBlock(pos, BlockLoader.HYPHACOTTA.block.defaultBlockState(), 3)
        return BurnableHypha.VanillaBehaviourHandler.CANCEL
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if(random.nextFloat() < EXPAND_RATE) {
            tryExpand(level, pos, random)
        }
    }

    private fun tryExpand(level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val randomX = random.nextIntBetweenInclusive(-1, 1)
        val randomY = random.nextIntBetweenInclusive(-1, 1)
        val randomZ = random.nextIntBetweenInclusive(-1, 1)
        if(randomX == 0 && randomY == 0 && randomZ == 0) {
            return
        }
        val newPos = pos.offset(randomX, randomY, randomZ)
        CommonUtil.tryExpandHypha(level, newPos)

        if(random.nextFloat() < GrandisporiaUtil.INITIAL_GROW_RATE) {
            GrandisporiaUtil.tryGrowInitialStipe(level, pos.above())
        }
        if(random.nextFloat() < TerraborerUtil.INITIAL_GROW_RATE) {
            TerraborerUtil.tryGrow(level, pos.above())
        }
    }

    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
        if (level.isClientSide) {
            return
        }

        AlienSoilUtil.onAlienSoilPlaced(level as ServerLevel, pos)
    }
}