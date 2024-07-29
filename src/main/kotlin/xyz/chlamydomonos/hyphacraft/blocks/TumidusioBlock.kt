package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.blocks.utils.HyphaCraftProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.plant.MycovastusUtil
import xyz.chlamydomonos.hyphacraft.utils.plant.TumidusioUtil
import xyz.chlamydomonos.hyphacraft.utils.plant.XenolichenUtil

class TumidusioBlock : Block(
    Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.SLIME_BLOCK).randomTicks()
), BurnableHypha {
    init {
        registerDefaultState(
            defaultBlockState()
                .setValue(HyphaCraftProperties.DENSITY, 1)
                .setValue(HyphaCraftProperties.EXPAND_X, 1)
                .setValue(HyphaCraftProperties.EXPAND_Y, 1)
                .setValue(HyphaCraftProperties.EXPAND_Z, 1)
        )
    }
    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        TumidusioUtil.expand(level, pos, state, random)
        for(i in -1..1) {
            for(j in -1..1) {
                for(k in -1..1) {
                    val newPos = pos.offset(i, j, k)
                    if(random.nextFloat() < TumidusioUtil.EXPAND_RATE && TumidusioUtil.canHyphaGrow(level, newPos)) {
                        TumidusioUtil.setHypha(level, newPos)
                    }
                }
            }
        }

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
        }
    }

    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        TumidusioUtil.expand(level, pos, state, random)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(HyphaCraftProperties.DENSITY)
        builder.add(HyphaCraftProperties.EXPAND_X)
        builder.add(HyphaCraftProperties.EXPAND_Y)
        builder.add(HyphaCraftProperties.EXPAND_Z)
    }

    override fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        neighborBlock: Block,
        neighborPos: BlockPos,
        movedByPiston: Boolean
    ) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston)
        if(level.isClientSide) {
            return
        }
        val density = state.getValue(HyphaCraftProperties.DENSITY)
        if(density > 1) {
            level.scheduleTick(pos, this, 1)
        }
    }

    override fun onBurnt(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        replacing: Boolean,
        random: RandomSource
    ): BurnableHypha.VanillaBehaviourHandler {
        if(replacing) {
            if(random.nextBoolean()) {
                level.setBlock(pos, BlockLoader.HYPHACOAL_BLOCK.block.defaultBlockState(), 3)
                return BurnableHypha.VanillaBehaviourHandler.CANCEL
            } else {
                return BurnableHypha.VanillaBehaviourHandler.DO
            }
        } else {
            if(random.nextInt(4) == 0) {
                return BurnableHypha.VanillaBehaviourHandler.DO
            } else {
                level.setBlock(pos, BlockLoader.HYPHACOAL_BLOCK.block.defaultBlockState(), 3)
                return BurnableHypha.VanillaBehaviourHandler.CANCEL
            }
        }
    }

    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 20

    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 100
}