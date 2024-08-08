package xyz.chlamydomonos.hyphacraft.blockentities.base

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class RouteBlockEntity(
    type: BlockEntityType<*>,
    pos: BlockPos,
    blockState: BlockState
) : BlockEntity(type, pos, blockState)  {
    abstract fun getNextPos(data: String): BlockPos
}