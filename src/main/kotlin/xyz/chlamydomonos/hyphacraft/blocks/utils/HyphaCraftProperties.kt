package xyz.chlamydomonos.hyphacraft.blocks.utils

import net.minecraft.world.level.block.state.properties.IntegerProperty

object HyphaCraftProperties {
    val PHASE = IntegerProperty.create("phase", 0, 14)
    val MUSHROOM_COUNT = IntegerProperty.create("mushroom_count", 1, 3)
}