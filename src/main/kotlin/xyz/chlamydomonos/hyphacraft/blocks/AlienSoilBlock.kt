package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class AlienSoilBlock : Block(Properties.ofFullCopy(Blocks.DIRT)), BurnableHypha {
    override fun onBurnt(state: BlockState, level: Level, pos: BlockPos, replacing: Boolean) {
        level.setBlock(pos, BlockLoader.HYPHACOTTA.block.defaultBlockState(), 3)
    }
}