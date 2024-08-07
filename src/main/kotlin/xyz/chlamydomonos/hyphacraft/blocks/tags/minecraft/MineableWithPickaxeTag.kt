package xyz.chlamydomonos.hyphacraft.blocks.tags.minecraft

import net.minecraft.tags.BlockTags
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class MineableWithPickaxeTag : BaseBlockTag(BlockTags.MINEABLE_WITH_PICKAXE) {
    override fun blocks() = listOf(
        BlockLoader.ALIEN_ROCK.block,
        BlockLoader.HYPHACOTTA.block,
        BlockLoader.HYPHACOAL_BLOCK.block
    )
}