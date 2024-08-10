package xyz.chlamydomonos.hyphacraft.blocks.tags.mod

import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag

class TumidusioHyphaReplaceableTag : BaseBlockTag("tumidusio_hypha_replaceable") {
    override fun blocks() = listOf(
        tag(BlockTags.LOGS_THAT_BURN),
        tag(BlockTags.LEAVES),
        block(Blocks.PUMPKIN),
        block(Blocks.CARVED_PUMPKIN),
        tag(BlockTags.PLANKS)
    )
}