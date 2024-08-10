package xyz.chlamydomonos.hyphacraft.blocks.tags.mod

import net.minecraft.world.level.block.Block
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class CarnivoravitisPlantTag : BaseBlockTag("carnivoravitis_plant") {
    override fun blocks() = listOf<Block>(
        BlockLoader.CARNIVORAVITIS_VINE,
        BlockLoader.CARNIVORAVITIS_SEEDLING,
        BlockLoader.CARNIVORAVITIS_ROOT,
        BlockLoader.CARNIVORAVITIS_SHELL.block
    )
}