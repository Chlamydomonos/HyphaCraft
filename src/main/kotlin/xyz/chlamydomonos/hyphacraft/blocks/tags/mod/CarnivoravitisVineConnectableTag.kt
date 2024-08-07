package xyz.chlamydomonos.hyphacraft.blocks.tags.mod

import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class CarnivoravitisVineConnectableTag : BaseBlockTag("carnivoravitis_vine_connectable") {
    override fun blocks() = listOf(
        BlockLoader.CARNIVORAVITIS_VINE
    )
}