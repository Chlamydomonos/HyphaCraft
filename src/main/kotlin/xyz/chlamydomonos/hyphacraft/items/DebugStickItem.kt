package xyz.chlamydomonos.hyphacraft.items

import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import xyz.chlamydomonos.hyphacraft.blocks.utils.HyphaCraftProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class DebugStickItem : Item(Properties()) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        if (level.isClientSide) {
            return InteractionResult.PASS
        }

        val pos = context.clickedPos
        level.setBlock(pos, BlockLoader.TUMIDUSIO.block.defaultBlockState().setValue(HyphaCraftProperties.DENSITY, 10), 3)
        level.scheduleTick(pos, BlockLoader.TUMIDUSIO.block, 1)

        return InteractionResult.SUCCESS
    }
}