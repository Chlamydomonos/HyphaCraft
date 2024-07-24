package xyz.chlamydomonos.hyphacraft.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class XenolichenBlockEntity(type: BlockEntityType<*>, pos: BlockPos, blockState: BlockState) :
    BlockEntity(type, pos, blockState) {
}