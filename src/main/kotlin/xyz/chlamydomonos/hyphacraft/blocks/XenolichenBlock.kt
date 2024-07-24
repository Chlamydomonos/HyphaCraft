package xyz.chlamydomonos.hyphacraft.blocks

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class XenolichenBlock : BaseEntityBlock(Properties.of()) {
    override fun codec(): MapCodec<out BaseEntityBlock> {
        TODO("Not yet implemented")
    }

    override fun newBlockEntity(p0: BlockPos, p1: BlockState): BlockEntity? {
        TODO("Not yet implemented")
    }
}