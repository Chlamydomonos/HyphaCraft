package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties

class XenolichenHiddenBlock : Block(
    Properties.ofFullCopy(Blocks.GLASS)
) {
    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.PHASE, 0))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.PHASE)
    }
}