package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.blocks.XenolichenBlock
import xyz.chlamydomonos.hyphacraft.blocks.XenolichenHiddenBlock

object BlockLoader {
    class BlockAndItsItem<T : Block>(
        blockHolder: DeferredHolder<Block, T>,
        itemHolder: DeferredHolder<Item, BlockItem>
    ) {
        val block by blockHolder
        val item by itemHolder
    }

    val BLOCKS = DeferredRegister.create(Registries.BLOCK, HyphaCraft.MODID)

    fun <T : Block> register(name: String, block: () -> T): BlockAndItsItem<T> {
        return register(name, 0, block)
    }

    fun <T : Block> register(name: String, priority: Int, block: () -> T): BlockAndItsItem<T> {
        val registeredBlock = BLOCKS.register(name, block)
        val registeredItem = ItemLoader.register(name, priority) { BlockItem(registeredBlock.get(), Item.Properties()) }
        return BlockAndItsItem(registeredBlock, registeredItem)
    }

    fun register(bus: IEventBus) {
        BLOCKS.register(bus)
    }

    val ALIEN_ROCK = register("alien_rock") { Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)) }
    val XENOLICHEN by BLOCKS.register("xenolichen", ::XenolichenBlock)
    val XENOLICHEN_HIDDEN_BLOCK by BLOCKS.register("xenolichen_hidden_block", ::XenolichenHiddenBlock)
}