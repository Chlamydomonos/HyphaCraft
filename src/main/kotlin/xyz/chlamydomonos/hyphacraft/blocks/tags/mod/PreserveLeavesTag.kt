package xyz.chlamydomonos.hyphacraft.blocks.tags.mod

import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class PreserveLeavesTag : BaseBlockTag("preserve_leaves") {
    override fun blocks() = listOf(block(BlockLoader.TUMIDUSIO_HYPHA))
}