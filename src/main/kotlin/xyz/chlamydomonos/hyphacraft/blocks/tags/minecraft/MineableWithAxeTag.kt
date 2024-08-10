package xyz.chlamydomonos.hyphacraft.blocks.tags.minecraft

import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class MineableWithAxeTag : BaseBlockTag(BlockTags.MINEABLE_WITH_AXE) {
    override fun blocks() = listOf<Block>(
        BlockLoader.GRANDISPORIA_WITHERED_STIPE.block,
        BlockLoader.GRANDISPORIA_WITHERED_CAP.block,
        BlockLoader.CARNIVORAVITIS_ROOT,
        BlockLoader.CARNIVORAVITIS_SHELL.block,
    )
}