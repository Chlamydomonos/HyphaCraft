package xyz.chlamydomonos.hyphacraft.items

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import xyz.chlamydomonos.hyphacraft.utils.plant.XenolichenUtil

class XenolichenItem : Item(Properties()) {
    override fun useOn(context: UseOnContext): InteractionResult {
        if(context.level.isClientSide) {
            return InteractionResult.PASS
        }

        val level = context.level as ServerLevel
        if (context.player?.abilities?.mayBuild == true && XenolichenUtil.canGrow(level, context.clickedPos)) {
            XenolichenUtil.setXenolichen(level, context.clickedPos)
            context.itemInHand.consume(1, context.player)
            return InteractionResult.SUCCESS
        }

        return InteractionResult.FAIL
    }
}