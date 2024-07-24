package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import java.util.PriorityQueue

object ItemLoader {
    class ItemInTab (
        val priority: Int,
        val index: Int,
        val item: DeferredHolder<Item, out Item>
    )

    val ITEMS = DeferredRegister.create(Registries.ITEM, HyphaCraft.MODID)

    val ITEMS_QUEUE = PriorityQueue(compareByDescending<ItemInTab>{it.priority}.thenBy{ it.index })

    private var globalIndex = 0

    fun <T : Item> register(name: String, item: () -> T): DeferredHolder<Item, T> {
        return register(name, 0, item)
    }

    fun <T : Item> register(name: String, priority: Int, item: () -> T): DeferredHolder<Item, T> {
        val registeredItem = ITEMS.register(name, item)
        ITEMS_QUEUE.add(ItemInTab(priority, globalIndex, registeredItem))
        globalIndex++
        return registeredItem
    }

    fun register(bus: IEventBus) {
        ITEMS.register(bus)
    }
}