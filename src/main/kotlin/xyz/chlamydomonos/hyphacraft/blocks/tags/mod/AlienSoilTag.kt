package xyz.chlamydomonos.hyphacraft.blocks.tags.mod

import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class AlienSoilTag : BaseBlockTag("alien_soil") {
    override fun blocks() = listOf(
        block(BlockLoader.ALIEN_SOIL.block),
        block(BlockLoader.ALIEN_SWARD.block),
        block(BlockLoader.FERTILE_ALIEN_SWARD.block),
        block(BlockLoader.ACTIVE_HYPHA_BLOCK.block)
    )
}