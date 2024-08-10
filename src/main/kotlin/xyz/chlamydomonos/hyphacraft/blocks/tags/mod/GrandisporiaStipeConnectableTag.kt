package xyz.chlamydomonos.hyphacraft.blocks.tags.mod

import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class GrandisporiaStipeConnectableTag : BaseBlockTag("grandisporia_stipe_connectable") {
    override fun blocks() = listOf(
        block(BlockLoader.GRANDISPORIA_STIPE),
        block(BlockLoader.GRANDISPORIA_CAP_CENTER),
        block(BlockLoader.GRANDISPORIA_WITHERED_STIPE.block),
        block(BlockLoader.LOOSE_FUNGUS_ROOT)
    )
}