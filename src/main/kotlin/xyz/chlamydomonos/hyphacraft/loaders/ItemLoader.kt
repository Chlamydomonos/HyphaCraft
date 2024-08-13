package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.items.AlienOrbItem
import xyz.chlamydomonos.hyphacraft.items.DebugStickItem
import xyz.chlamydomonos.hyphacraft.items.XenolichenItem
import java.util.*

object ItemLoader {
    class ItemInTab (
        val priority: Int,
        val index: Int,
        val item: DeferredHolder<Item, out Item>
    )

    private val ITEMS = DeferredRegister.create(Registries.ITEM, HyphaCraft.MODID)

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

    private fun simpleItem(name: String, priority: Int = 0) = register(name, priority) { Item(Item.Properties()) }

    fun register(bus: IEventBus) {
        ITEMS.register(bus)
    }

    val DEBUG_STICK by register("debug_stick", ::DebugStickItem)
    val XENOLICHEN by register("xenolichen", ::XenolichenItem)
    val ROTTEN_FUNGUS_BALL by simpleItem("rotten_fungus_ball")
    val MOLDY_CORK_DUST by simpleItem("moldy_cork_dust")
    val TUBULAR_HYPHA by simpleItem("tubular_hypha")
    val WHITE_HYPHA by simpleItem("white_hypha")
    val SPORE_POWDER by simpleItem("spore_powder")
    val HUMUS by simpleItem("humus")
    val ALIEN_ORB by register("alien_orb", ::AlienOrbItem)
    val CARNIVORAVITIS_FLOWER by simpleItem("carnivoravitis_flower")
    val CARNIVORAVITIS_ROOT by simpleItem("carnivoravitis_root")
    val TOXIC_SPORE_POWDER by simpleItem("toxic_spore_powder")
}