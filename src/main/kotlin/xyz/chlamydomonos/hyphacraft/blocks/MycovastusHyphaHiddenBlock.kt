package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties

class MycovastusHyphaHiddenBlock : Block(Properties.ofFullCopy(Blocks.GLASS)) {
    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.PHASE)
    }
}