package xyz.chlamydomonos.hyphacraft.blocks.tags.mod

import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag

class MycovastusHyphaReplaceableTag : BaseBlockTag("mycovastus_hypha_replaceable") {
    override fun blocks() = listOf(
        tag(BlockTags.DIRT),
        block(Blocks.MOSS_BLOCK),
        block(Blocks.CLAY)
    )
}