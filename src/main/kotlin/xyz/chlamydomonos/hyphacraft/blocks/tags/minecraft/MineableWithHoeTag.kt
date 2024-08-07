package xyz.chlamydomonos.hyphacraft.blocks.tags.minecraft

import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class MineableWithHoeTag : BaseBlockTag(BlockTags.MINEABLE_WITH_HOE) {
    override fun blocks() = listOf<Block>(
        BlockLoader.TUMIDUSIO.block,
        BlockLoader.TUMIDUSIO_HYPHA,
        BlockLoader.XENOLICHEN_BLOCK,
        BlockLoader.MYCOVASTUS_HYPHA,
        BlockLoader.GRANDISPORIA_STIPE,
        BlockLoader.GRANDISPORIA_SMALL_CAP,
        BlockLoader.GRANDISPORIA_CAP_CENTER,
        BlockLoader.GRANDISPORIA_CAP
    )
}