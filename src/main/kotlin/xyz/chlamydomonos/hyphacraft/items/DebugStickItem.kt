package xyz.chlamydomonos.hyphacraft.items

import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.Block
import xyz.chlamydomonos.hyphacraft.blockentities.CarnivoravitisVineBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.datacomponents.BlockHolder
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.DataComponentLoader
import xyz.chlamydomonos.hyphacraft.loaders.EntityLoader

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
            BlockLoader.GRANDISPORIA_STIPE,
            BlockLoader.TERRABORER_STIPE.block,
            BlockLoader.CARNIVORAVITIS_VINE
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
        val pos0 = context.clickedPos.above()
        var pos = pos0.above()
        for (i in 1..10) {
            pos = pos.above()
            level.setBlock(pos, BlockLoader.CARNIVORAVITIS_VINE.defaultBlockState(), 3)
            val be = level.getBlockEntity(pos) as CarnivoravitisVineBlockEntity
            be.nextPos = pos.below()
        }
        for (i in 1..10) {
            pos = pos.offset(1, 0, 0)
            level.setBlock(pos, BlockLoader.CARNIVORAVITIS_VINE.defaultBlockState(), 3)
            val be = level.getBlockEntity(pos) as CarnivoravitisVineBlockEntity
            be.nextPos = pos.offset(-1, 0, 0)
        }
        pos = pos.above()
        level.setBlock(pos, BlockLoader.CARNIVORAVITIS_FLOWER.defaultBlockState().setValue(ModProperties.DIRECTION, Direction.UP), 3)

        return InteractionResult.SUCCESS
    }

    override fun interactLivingEntity(
        stack: ItemStack,
        player: Player,
        interactionTarget: LivingEntity,
        usedHand: InteractionHand
    ): InteractionResult {
        if (player.level().isClientSide) {
            return InteractionResult.PASS
        }

        val entity = EntityLoader.TRANSPORT.create(player.level())!!
        entity.setPos(interactionTarget.position())
        player.level().addFreshEntity(entity)
        interactionTarget.startRiding(entity, true)

        return InteractionResult.SUCCESS
    }

    override fun getName(stack: ItemStack): Component {
        val block = stack.get(DataComponentLoader.BLOCK_HOLDER)!!.block
        return super.getName(stack).copy().append(": ").append(block.name)
    }
}