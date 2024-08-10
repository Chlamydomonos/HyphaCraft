package xyz.chlamydomonos.hyphacraft.blocks.tags.minecraft

import net.minecraft.tags.BlockTags
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class MineableWithShovelTag : BaseBlockTag(BlockTags.MINEABLE_WITH_SHOVEL) {
    override fun blocks() = listOf(
        block(BlockLoader.ALIEN_SOIL.block),
        block(BlockLoader.ROTTEN_FUNGUS_HEAP.block),
        block(BlockLoader.SPORE_HEAP.block),
        block(BlockLoader.HUMUS_HEAP.block)
    )
}