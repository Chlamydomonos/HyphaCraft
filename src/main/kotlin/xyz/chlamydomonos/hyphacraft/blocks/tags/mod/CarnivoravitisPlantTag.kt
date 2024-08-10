package xyz.chlamydomonos.hyphacraft.blocks.tags.mod

import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class CarnivoravitisPlantTag : BaseBlockTag("carnivoravitis_plant") {
    override fun blocks() = listOf(
        block(BlockLoader.CARNIVORAVITIS_VINE),
        block(BlockLoader.CARNIVORAVITIS_SEEDLING),
        block(BlockLoader.CARNIVORAVITIS_ROOT),
        block(BlockLoader.CARNIVORAVITIS_SHELL.block)
    )
}