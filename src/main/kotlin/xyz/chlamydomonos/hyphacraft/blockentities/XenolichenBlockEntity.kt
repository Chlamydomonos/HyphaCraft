package xyz.chlamydomonos.hyphacraft.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.loaders.BlockEntityLoader

class XenolichenBlockEntity(
    pos: BlockPos,
    blockState: BlockState
) : BlockCopierEntity(BlockEntityLoader.XENOLICHEN, pos, blockState)