package xyz.chlamydomonos.hyphacraft.blocks.tags.mod

import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag

class TumidusioReplaceableTag : BaseBlockTag("tumidusio_replaceable") {
    override fun blocks() = listOf(
        tag(BlockTags.REPLACEABLE),
        block(Blocks.MOSS_CARPET)
    )
}