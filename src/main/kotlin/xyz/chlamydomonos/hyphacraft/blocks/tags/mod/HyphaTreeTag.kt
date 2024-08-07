package xyz.chlamydomonos.hyphacraft.blocks.tags.mod

import net.minecraft.world.level.block.Block
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class HyphaTreeTag : BaseBlockTag("hypha_tree") {
    override fun blocks() = listOf<Block>(
        BlockLoader.GRANDISPORIA_STIPE,
        BlockLoader.GRANDISPORIA_WITHERED_STIPE.block,
        BlockLoader.LOOSE_FUNGUS_ROOT
    )
}