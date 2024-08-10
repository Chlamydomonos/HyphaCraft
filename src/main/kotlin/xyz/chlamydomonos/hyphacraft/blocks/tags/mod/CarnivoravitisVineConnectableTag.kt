package xyz.chlamydomonos.hyphacraft.blocks.tags.mod

import net.minecraft.world.level.block.Block
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class CarnivoravitisVineConnectableTag : BaseBlockTag("carnivoravitis_vine_connectable") {
    override fun blocks() = listOf<Block>(
        BlockLoader.CARNIVORAVITIS_VINE,
        BlockLoader.CARNIVORAVITIS_ROOT
    )
}