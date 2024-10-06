package xyz.chlamydomonos.hyphacraft.items

import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.PipeBlock
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.blockentities.base.BlockCopierEntity
import xyz.chlamydomonos.hyphacraft.datacomponents.BlockHolder
import xyz.chlamydomonos.hyphacraft.entity.entities.HumifossorEntity
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.DataComponentLoader
import xyz.chlamydomonos.hyphacraft.utils.plant.CarnivoravitisUtil

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
            BlockLoader.CARNIVORAVITIS_VINE,
            BlockLoader.VERMILINGUA,
            BlockLoader.PULVERIUM.block,
            BlockLoader.FULGURFUNGUS.block
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

        if (level.getBlockState(context.clickedPos).`is`(BlockLoader.TEST_BLOCK)) {
            level.scheduleTick(context.clickedPos, BlockLoader.TEST_BLOCK, 1)
            return InteractionResult.SUCCESS
        }

        if (level.getBlockState(context.clickedPos).block in BLOCK_COPIERS) {
            return InteractionResult.SUCCESS
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
        when (block) {
            in BLOCK_COPIERS -> {
                val oldState = level.getBlockState(pos)
                level.setBlock(pos, block.defaultBlockState(), 3)
                val be = level.getBlockEntity(pos) as BlockCopierEntity
                be.copiedState = oldState
            }
            BlockLoader.CARNIVORAVITIS_VINE -> {
                CarnivoravitisUtil.tryGrowInitial(level as ServerLevel, pos)
            }
            in PLANTS -> {
                var state = block.defaultBlockState()
                val face = context.clickedFace
                val newPos = pos.offset(face.normal)
                val property = PipeBlock.PROPERTY_BY_DIRECTION[face.opposite]!!
                if (state.hasProperty(property)) {
                    state = state.setValue(property, true)
                }
                level.setBlock(newPos, state, 3)
            }
        }

        return InteractionResult.SUCCESS
    }

    override fun getName(stack: ItemStack): Component {
        val block = stack.get(DataComponentLoader.BLOCK_HOLDER)!!.block
        return super.getName(stack).copy().append(": ").append(block.name)
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

        if (interactionTarget !is HumifossorEntity) {
            return InteractionResult.PASS
        }

        if (player.isShiftKeyDown) {
            interactionTarget.charged = !interactionTarget.charged
        } else {
            HyphaCraft.LOGGER.debug("state: {}", interactionTarget.state)
        }

        return InteractionResult.SUCCESS
    }
}