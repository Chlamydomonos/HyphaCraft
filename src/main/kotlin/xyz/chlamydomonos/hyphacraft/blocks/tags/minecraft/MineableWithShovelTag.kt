package xyz.chlamydomonos.hyphacraft.blocks.tags.minecraft

import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class MineableWithShovelTag : BaseBlockTag(BlockTags.MINEABLE_WITH_SHOVEL) {
    override fun blocks() = listOf<Block>(
        BlockLoader.ALIEN_SOIL.block,
        BlockLoader.ROTTEN_FUNGUS_HEAP.block,
        BlockLoader.SPORE_HEAP.block,
        BlockLoader.HUMUS_HEAP.block
    )
}