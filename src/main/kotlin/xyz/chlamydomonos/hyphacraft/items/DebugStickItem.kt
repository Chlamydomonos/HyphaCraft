package xyz.chlamydomonos.hyphacraft.items

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import xyz.chlamydomonos.hyphacraft.datacomponents.BlockHolder
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.DataComponentLoader
import xyz.chlamydomonos.hyphacraft.utils.plant.CarnivoravitisUtil

class DebugStickItem : Item(
    Properties().component(DataComponentLoader.BLOCK_HOLDER, BlockHolder(BlockLoader.XENOLICHEN_BLOCK))
) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        if (level.isClientSide) {
            return InteractionResult.PASS
        }

        CarnivoravitisUtil.tryGrowInitial(level as ServerLevel, context.clickedPos)

        return InteractionResult.SUCCESS
    }
}