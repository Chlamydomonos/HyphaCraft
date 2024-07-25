package xyz.chlamydomonos.hyphacraft.items

import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import xyz.chlamydomonos.hyphacraft.blockentities.XenolichenBlockEntity
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class DebugStickItem : Item(Properties()) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        if (level.isClientSide) {
            return InteractionResult.PASS
        }

        val pos = context.clickedPos
        val state = level.getBlockState(pos)

        level.setBlock(pos, BlockLoader.XENOLICHEN.defaultBlockState(), 0)

        val be = level.getBlockEntity(pos) as XenolichenBlockEntity
        be.copiedState = state

        level.sendBlockUpdated(pos, state, level.getBlockState(pos), 2)

        return InteractionResult.SUCCESS
    }
}