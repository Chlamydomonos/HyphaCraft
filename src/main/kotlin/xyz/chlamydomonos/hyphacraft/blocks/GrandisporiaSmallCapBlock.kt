package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties

class GrandisporiaSmallCapBlock : Block(
    Properties.ofFullCopy(Blocks.DIRT)
        .noOcclusion()
        .sound(SoundType.SLIME_BLOCK)
        .randomTicks()
) {
    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.CAN_GROW, false))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.CAN_GROW)
    }
}