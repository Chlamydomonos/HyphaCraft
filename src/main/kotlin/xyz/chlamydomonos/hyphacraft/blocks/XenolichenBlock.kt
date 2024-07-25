package xyz.chlamydomonos.hyphacraft.blocks

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.MapColor
import xyz.chlamydomonos.hyphacraft.blockentities.XenolichenBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.utils.HyphaCraftProperties

class XenolichenBlock : BaseEntityBlock(
    Properties.ofFullCopy(Blocks.DIRT)
        .mapColor(MapColor.PLANT)
        .instabreak()
        .sound(SoundType.GRASS)
        .ignitedByLava()
        .randomTicks()
) {
    init {
        registerDefaultState(defaultBlockState().setValue(HyphaCraftProperties.PHASE, 0))
    }

    companion object {
        val CODEC: MapCodec<XenolichenBlock> = simpleCodec { XenolichenBlock() }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(HyphaCraftProperties.PHASE)
    }

    override fun codec() = CODEC

    override fun newBlockEntity(p0: BlockPos, p1: BlockState) = XenolichenBlockEntity(p0, p1)

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
    }
}