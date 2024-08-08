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
import xyz.chlamydomonos.hyphacraft.blockentities.MycovastusHyphaBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaEntityBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.plant.MycovastusUtil

class MycovastusHyphaBlock : BaseHyphaEntityBlock(
    Properties.ofFullCopy(Blocks.DIRT)
        .mapColor(MapColor.PLANT)
        .ignitedByLava()
        .randomTicks()
        .sound(SoundType.SLIME_BLOCK)
) {
    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.PHASE, 0))
    }

    companion object {
        val CODEC = simpleCodec { MycovastusHyphaBlock() }
    }
    override fun codec() = CODEC

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = MycovastusHyphaBlockEntity(pos, state)

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.PHASE)
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
            val be = level.getBlockEntity(pos) as MycovastusHyphaBlockEntity
            level.setBlock(pos, be.copiedState, 3)
        } else {
            level.setBlock(pos, BlockLoader.HYPHACOTTA.block.defaultBlockState(), 3)
        }
        return BurnableHypha.VanillaBehaviourHandler.CANCEL
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val phase = state.getValue(ModProperties.PHASE)
        for (i in -1..1) {
            for(j in -1..1) {
                for(k in -1..1) {
                    val newPos = pos.offset(i, j, k)
                    if(random.nextFloat() < MycovastusUtil.EXPAND_RATE && MycovastusUtil.canHyphaGrow(level, newPos)) {
                        MycovastusUtil.setHypha(level, newPos)
                    }
                }
            }
        }
        if(random.nextFloat() < MycovastusUtil.MUSHROOM_RATE && level.getBlockState(pos.above()).isEmpty) {
            MycovastusUtil.growMushroom(level, pos, random)
        }
        if(phase < 14) {
            MycovastusUtil.setHypha(level, pos, phase + 1)
        } else {
            val abovePos = pos.above()
            if (level.getBlockState(abovePos).`is`(BlockLoader.MYCOVASTUS.block)) {
                level.setBlock(abovePos, BlockLoader.ROTTEN_FUNGUS_HEAP.block.defaultBlockState(), 3)
            }
            level.setBlock(pos, BlockLoader.ALIEN_SOIL.block.defaultBlockState(), 3)
        }
    }
}