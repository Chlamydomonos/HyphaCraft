package xyz.chlamydomonos.hyphacraft.blocks.tags.minecraft

import net.minecraft.tags.BlockTags
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class MineableWithHoeTag : BaseBlockTag(BlockTags.MINEABLE_WITH_HOE) {
    override fun blocks() = listOf(
        block(BlockLoader.TUMIDUSIO.block),
        block(BlockLoader.TUMIDUSIO_HYPHA),
        block(BlockLoader.XENOLICHEN_BLOCK),
        block(BlockLoader.MYCOVASTUS_HYPHA),
        block(BlockLoader.GRANDISPORIA_STIPE),
        block(BlockLoader.GRANDISPORIA_SMALL_CAP),
        block(BlockLoader.GRANDISPORIA_CAP_CENTER),
        block(BlockLoader.GRANDISPORIA_CAP),
        block(BlockLoader.CARNIVORAVITIS_VINE)
    )
}