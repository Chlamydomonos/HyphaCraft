package xyz.chlamydomonos.hyphacraft.blocks.tags.mod

import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag

class XenolichenReplaceableTag : BaseBlockTag("xenolichen_replaceable") {
    override fun blocks() = listOf(
        tag(BlockTags.BASE_STONE_OVERWORLD),
        tag(Tags.Blocks.SANDSTONE_BLOCKS),
        tag(Tags.Blocks.SANDS),
        tag(BlockTags.TERRACOTTA),
        tag(BlockTags.CONCRETE_POWDER),
        tag(Tags.Blocks.CONCRETES),
        tag(Tags.Blocks.ORES),
        block(Blocks.GRAVEL),
        tag(BlockTags.STONE_BRICKS),
        tag(Tags.Blocks.COBBLESTONES)
    )
}