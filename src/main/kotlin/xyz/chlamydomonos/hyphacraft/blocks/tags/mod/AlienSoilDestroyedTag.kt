package xyz.chlamydomonos.hyphacraft.blocks.tags.mod

import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag

class AlienSoilDestroyedTag : BaseBlockTag("alien_soil_destroyed") {
    override fun blocks() = listOf(
        block(Blocks.MOSS_CARPET),
        tag(BlockTags.SNOW)
    )
}