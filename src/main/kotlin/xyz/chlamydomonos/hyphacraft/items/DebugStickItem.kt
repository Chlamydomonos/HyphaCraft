package xyz.chlamydomonos.hyphacraft.items

import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.blockentities.XenolichenBlockEntity
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class DebugStickItem : Item(Properties()) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        if (level.isClientSide) {
            val be = level.getBlockEntity(context.clickedPos)
            if(be is XenolichenBlockEntity) {
                val s = be.copiedState.block.name
                HyphaCraft.LOGGER.debug("Xenolichen: {}", s)
            }
            return InteractionResult.PASS
        }

        val pos = context.clickedPos
        val state = level.getBlockState(pos)

        if(state.block != BlockLoader.XENOLICHEN) {
            level.setBlock(pos, BlockLoader.XENOLICHEN.defaultBlockState(), 0)

            val be = level.getBlockEntity(pos) as XenolichenBlockEntity
            be.copiedState = state

            level.sendBlockUpdated(pos, state, level.getBlockState(pos), 2)
        }
        HyphaCraft.LOGGER.debug("light level: {}", level.getRawBrightness(pos, 0))
        return InteractionResult.SUCCESS
    }
}