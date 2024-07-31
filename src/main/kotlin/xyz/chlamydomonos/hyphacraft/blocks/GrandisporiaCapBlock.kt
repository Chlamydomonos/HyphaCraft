package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.Direction
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition

class GrandisporiaCapBlock : HorizontalDirectionalBlock(
    Properties.ofFullCopy(Blocks.DIRT)
        .sound(SoundType.SLIME_BLOCK)
        .noOcclusion()
) {
    init {
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH))
    }

    companion object {
        val CODEC = simpleCodec { GrandisporiaCapBlock() }
    }

    override fun codec() = CODEC

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }
}