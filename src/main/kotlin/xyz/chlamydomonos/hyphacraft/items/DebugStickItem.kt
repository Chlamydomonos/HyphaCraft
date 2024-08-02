package xyz.chlamydomonos.hyphacraft.items

import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import xyz.chlamydomonos.hyphacraft.blockentities.BlockCopierEntity
import xyz.chlamydomonos.hyphacraft.datacomponents.BlockHolder
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.DataComponentLoader

class DebugStickItem : Item(
    Properties().component(DataComponentLoader.BLOCK_HOLDER, BlockHolder(BlockLoader.XENOLICHEN_BLOCK))
) {
    companion object {
        val BLOCK_COPIERS = listOf<Block>(
            BlockLoader.XENOLICHEN_BLOCK,
            BlockLoader.MYCOVASTUS_HYPHA,
            BlockLoader.TUMIDUSIO_HYPHA
        )

        val PLANTS = listOf<Block>(
            BlockLoader.GRANDISPORIA_STIPE
        )

        val BLOCKS = mutableListOf<Block>()

        init {
            BLOCKS.addAll(BLOCK_COPIERS)
            BLOCKS.addAll(PLANTS)
        }
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        if (level.isClientSide) {
            return InteractionResult.PASS
        }
        val blockHolder = context.itemInHand.get(DataComponentLoader.BLOCK_HOLDER)!!
        val block = blockHolder.block
        val blockId = BLOCKS.indexOf(block)

        if(context.player!!.isShiftKeyDown) {
            val newBlock = BLOCKS[(blockId + 1) % BLOCKS.size]
            context.itemInHand.set(DataComponentLoader.BLOCK_HOLDER, BlockHolder(newBlock))
            return InteractionResult.SUCCESS
        }

        val pos = context.clickedPos
        if (block in BLOCK_COPIERS) {
            val oldState = level.getBlockState(pos)
            level.setBlock(pos, block.defaultBlockState(), 3)
            val be = level.getBlockEntity(pos) as BlockCopierEntity
            be.copiedState = oldState
        } else if (block in PLANTS) {
            var state = block.defaultBlockState()
            if (state.hasProperty(BlockStateProperties.DOWN)) {
                state = state.setValue(BlockStateProperties.DOWN, true)
            }
            level.setBlock(pos.above(), state, 3)
        }

        return InteractionResult.SUCCESS
    }

    override fun getName(stack: ItemStack): Component {
        val block = stack.get(DataComponentLoader.BLOCK_HOLDER)!!.block
        return super.getName(stack).copy().append(": ").append(block.name)
    }
}