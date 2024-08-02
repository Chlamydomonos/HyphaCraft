package xyz.chlamydomonos.hyphacraft.items

import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class DebugStickItem : Item(Properties()) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        if (level.isClientSide) {
            return InteractionResult.PASS
        }

        val pos = context.clickedPos
        level.setBlock(pos, BlockLoader.GRANDISPORIA_STIPE.defaultBlockState(), 3)

        return InteractionResult.SUCCESS
    }
}