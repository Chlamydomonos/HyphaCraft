package xyz.chlamydomonos.hyphacraft.blocks.utils

import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty

object ModProperties {
    val PHASE = IntegerProperty.create("phase", 0, 14)
    val MUSHROOM_COUNT = IntegerProperty.create("mushroom_count", 1, 3)
    val DENSITY = IntegerProperty.create("density", 1, 32)
    val EXPAND_X = IntegerProperty.create("expand_x", 0, 2)
    val EXPAND_Y = IntegerProperty.create("expand_y", 0, 2)
    val EXPAND_Z = IntegerProperty.create("expand_z", 0, 2)
    val AGE = IntegerProperty.create("age", 0, 3)
    val HEIGHT = IntegerProperty.create("height", 0, 4)
    val CAN_GROW = BooleanProperty.create("can_grow")
    val SPORE_AMOUNT = IntegerProperty.create("spore_amount", 0, 3)
    val IS_CORNER = BooleanProperty.create("is_corner")
    val CONTAINS_WATER = BooleanProperty.create("contains_water")
    val DIRECTION = DirectionProperty.create("direction")
    val CAN_SECRETE = BooleanProperty.create("can_secrete")
}