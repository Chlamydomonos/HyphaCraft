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
import net.minecraft.world.level.material.MapColor
import xyz.chlamydomonos.hyphacraft.blockentities.XenolichenBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaEntityBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.plant.XenolichenUtil

class XenolichenBlock : BaseHyphaEntityBlock(
    Properties.ofFullCopy(Blocks.DIRT)
        .mapColor(MapColor.PLANT)
        .sound(SoundType.SLIME_BLOCK)
        .ignitedByLava()
        .randomTicks()
        .mapColor(MapColor.COLOR_BLUE)
) {
    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.PHASE, 0))
    }

    companion object {
        val CODEC = simpleCodec { XenolichenBlock() }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.PHASE)
    }

    override fun codec() = CODEC

    override fun newBlockEntity(p0: BlockPos, p1: BlockState) = XenolichenBlockEntity(p0, p1)

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val phase = state.getValue(ModProperties.PHASE)
        for (i in -1..1) {
            for (j in -1..1) {
                for (k in -1..1) {
                    val newPos = pos.offset(i, j, k)
                    if(random.nextFloat() < XenolichenUtil.EXPAND_RATE &&  XenolichenUtil.canGrow(level, newPos)) {
                        XenolichenUtil.setXenolichen(level, newPos)
                    }
                }
            }
        }
        if(phase < 14) {
            XenolichenUtil.setXenolichen(level, pos, phase + 1)
        } else {
            level.setBlock(pos, BlockLoader.ALIEN_SOIL.block.defaultBlockState(), 3)
        }
    }

    override fun onBurnt(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        replacing: Boolean,
        random: RandomSource
    ): BurnableHypha.VanillaBehaviourHandler {
        val phase = state.getValue(ModProperties.PHASE)
        if(phase < 10) {
            val be = level.getBlockEntity(pos) as XenolichenBlockEntity
            level.setBlock(pos, be.copiedState, 3)
        } else {
            level.setBlock(pos, BlockLoader.HYPHACOTTA.block.defaultBlockState(), 3)
        }
        return BurnableHypha.VanillaBehaviourHandler.CANCEL
    }
}