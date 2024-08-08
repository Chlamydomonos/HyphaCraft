package xyz.chlamydomonos.hyphacraft.blocks.tags.mod

import net.minecraft.world.level.block.Block
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class GrandisporiaStipeConnectableTag : BaseBlockTag("grandisporia_stipe_connectable") {
    override fun blocks() = listOf<Block>(
        BlockLoader.GRANDISPORIA_STIPE,
        BlockLoader.GRANDISPORIA_CAP_CENTER,
        BlockLoader.GRANDISPORIA_WITHERED_STIPE.block,
        BlockLoader.LOOSE_FUNGUS_ROOT
    )
}