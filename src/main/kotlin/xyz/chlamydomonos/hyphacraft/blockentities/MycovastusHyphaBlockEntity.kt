package xyz.chlamydomonos.hyphacraft.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.blockentities.base.BlockCopierEntity
import xyz.chlamydomonos.hyphacraft.loaders.BlockEntityLoader

class MycovastusHyphaBlockEntity(
    pos: BlockPos,
    blockState: BlockState
) : BlockCopierEntity(BlockEntityLoader.MYCOVASTUS_HYPHA, pos, blockState)