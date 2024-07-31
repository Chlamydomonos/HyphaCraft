package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.plant.GrandisporiaUtil
import xyz.chlamydomonos.hyphacraft.utils.plant.MycovastusUtil
import xyz.chlamydomonos.hyphacraft.utils.plant.TumidusioUtil
import xyz.chlamydomonos.hyphacraft.utils.plant.XenolichenUtil

class AlienSoilBlock : Block(Properties.ofFullCopy(Blocks.DIRT).randomTicks()), BurnableHypha {
    companion object {
        val EXPAND_RATE = 1.0f / 20.0f
    }

    override fun onBurnt(state: BlockState, level: Level, pos: BlockPos, replacing: Boolean, random: RandomSource): BurnableHypha.VanillaBehaviourHandler {
        level.setBlock(pos, BlockLoader.HYPHACOTTA.block.defaultBlockState(), 3)
        return BurnableHypha.VanillaBehaviourHandler.CANCEL
    }

    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 20

    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 100

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
        if(XenolichenUtil.canGrow(level, newPos)) {
            XenolichenUtil.setXenolichen(level, newPos)
        } else if (MycovastusUtil.canHyphaGrow(level, newPos)) {
            MycovastusUtil.setHypha(level, newPos)
        } else if (TumidusioUtil.canHyphaGrow(level, newPos)) {
            TumidusioUtil.setHypha(level, newPos)
        }

        if(random.nextFloat() < GrandisporiaUtil.INITIAL_GROW_RATE) {
            GrandisporiaUtil.tryGrowInitialStipe(level, pos.above())
        }
    }
}