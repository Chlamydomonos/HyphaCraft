package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties

class GrandisporiaWitheredCapBlock : Block(
    Properties.ofFullCopy(Blocks.DIRT)
        .sound(SoundType.FUNGUS)
        .randomTicks()
) {
    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.SPORE_AMOUNT, 0))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.SPORE_AMOUNT)
    }
}