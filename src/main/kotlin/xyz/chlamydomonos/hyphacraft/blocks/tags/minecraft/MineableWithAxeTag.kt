package xyz.chlamydomonos.hyphacraft.blocks.tags.minecraft

import net.minecraft.tags.BlockTags
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class MineableWithAxeTag : BaseBlockTag(BlockTags.MINEABLE_WITH_AXE) {
    override fun blocks() = listOf(
        block(BlockLoader.GRANDISPORIA_WITHERED_STIPE.block),
        block(BlockLoader.GRANDISPORIA_WITHERED_CAP.block),
        block(BlockLoader.CARNIVORAVITIS_ROOT),
        block(BlockLoader.CARNIVORAVITIS_SHELL.block),
    )
}