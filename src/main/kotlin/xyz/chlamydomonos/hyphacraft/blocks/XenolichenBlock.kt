package xyz.chlamydomonos.hyphacraft.blocks

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.MapColor
import xyz.chlamydomonos.hyphacraft.blockentities.XenolichenBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.blocks.utils.HyphaCraftProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class XenolichenBlock : BaseEntityBlock(
    Properties.ofFullCopy(Blocks.DIRT)
        .mapColor(MapColor.PLANT)
        .instabreak()
        .sound(SoundType.GRASS)
        .ignitedByLava()
        .randomTicks()
), BurnableHypha {
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

    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 20

    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5

    override fun onBurnt(state: BlockState, level: Level, pos: BlockPos, replacing: Boolean) {
        val phase = state.getValue(HyphaCraftProperties.PHASE)
        if(phase < 10) {
            val be = level.getBlockEntity(pos) as XenolichenBlockEntity
            level.setBlock(pos, be.copiedState, 3)
        } else {
            level.setBlock(pos, BlockLoader.HYPHACOTTA.block.defaultBlockState(), 3)
        }
    }
}