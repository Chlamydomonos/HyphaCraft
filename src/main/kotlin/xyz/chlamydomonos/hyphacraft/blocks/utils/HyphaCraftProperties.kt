package xyz.chlamydomonos.hyphacraft.blocks.utils

import net.minecraft.world.level.block.state.properties.IntegerProperty

object HyphaCraftProperties {
    val PHASE = IntegerProperty.create("phase", 0, 14)
    val MUSHROOM_COUNT = IntegerProperty.create("mushroom_count", 1, 3)
    val DENSITY = IntegerProperty.create("density", 1, 32)
    val EXPAND_X = IntegerProperty.create("expand_x", 0, 2)
    val EXPAND_Y = IntegerProperty.create("expand_y", 0, 2)
    val EXPAND_Z = IntegerProperty.create("expand_z", 0, 2)
}