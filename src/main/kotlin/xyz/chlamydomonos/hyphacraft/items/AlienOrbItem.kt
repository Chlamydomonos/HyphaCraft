package xyz.chlamydomonos.hyphacraft.items

import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.items.utils.StyleUtil
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class AlienOrbItem : Item(Properties()) {
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
        tooltipComponents.add(Component.translatable("tooltip.hyphacraft.alien_orb").withStyle(StyleUtil.TOOLTIP))
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        if(!context.level.isClientSide) {
            val block = context.level.getBlockState(context.clickedPos).block
            if (block is BurnableHypha) {
                context.level.setBlock(context.clickedPos, BlockLoader.ALIEN_EXPLOSIVE.defaultBlockState(), 3)
                return InteractionResult.SUCCESS
            }
        }

        return Items.FLINT_AND_STEEL.useOn(context)
    }
}