package xyz.chlamydomonos.hyphacraft.blocks.tags.mod

import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class HyphaTreeTag : BaseBlockTag("hypha_tree") {
    override fun blocks() = listOf(
        block(BlockLoader.GRANDISPORIA_STIPE),
        block(BlockLoader.GRANDISPORIA_WITHERED_STIPE.block),
        block(BlockLoader.LOOSE_FUNGUS_ROOT)
    )
}