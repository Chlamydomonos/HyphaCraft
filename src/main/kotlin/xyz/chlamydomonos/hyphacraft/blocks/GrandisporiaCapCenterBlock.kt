package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType

class GrandisporiaCapCenterBlock : Block(
    Properties.ofFullCopy(Blocks.DIRT)
        .sound(SoundType.SLIME_BLOCK)
        .randomTicks()
) {
}