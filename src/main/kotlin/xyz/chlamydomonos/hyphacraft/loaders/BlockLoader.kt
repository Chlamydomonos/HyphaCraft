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
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import kotlin.reflect.KProperty

object BlockLoader {
    class BlockAndItsItem<T : Block>(
        val block: T,
        val item: BlockItem
    )
    class LoadedBlock<T : Block>(
        private val block: DeferredHolder<Block, T>,
        private val item: DeferredHolder<Item, BlockItem>
    ) {
        private val blockAndItsItem by lazy { BlockAndItsItem(block.get(), item.get()) }
        operator fun getValue(thisRef: Any?, property: KProperty<*>) = blockAndItsItem
    }

    val BLOCKS = DeferredRegister.create(Registries.BLOCK, HyphaCraft.MODID)

    val ALIEN_ROCK by register("alien_rock") { Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)) }

    fun <T : Block> register(name: String, block: () -> T): LoadedBlock<T> {
        return register(name, 0, block)
    }

    fun <T : Block> register(name: String, priority: Int, block: () -> T): LoadedBlock<T> {
        val registeredBlock = BLOCKS.register(name, block)
        val registeredItem = ItemLoader.register(name, priority) { BlockItem(registeredBlock.get(), Item.Properties()) }
        return LoadedBlock(registeredBlock, registeredItem)
    }

    fun register(bus: IEventBus) {
        BLOCKS.register(bus)
    }
}