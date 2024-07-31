package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.client.color.block.BlockColor
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.blocks.*

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
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

    private fun copy(block: Block) = BlockBehaviour.Properties.ofFullCopy(block)

    val TEST_BLOCK by BLOCKS.register("test_block", ::TestBlock)
    val ALIEN_ROCK = register("alien_rock") { Block(copy(Blocks.STONE)) }
    val ALIEN_SOIL = register("alien_soil", ::AlienSoilBlock)
    val XENOLICHEN_BLOCK by BLOCKS.register("xenolichen_block", ::XenolichenBlock)
    val XENOLICHEN_HIDDEN_BLOCK by BLOCKS.register("xenolichen_hidden_block", ::XenolichenHiddenBlock)
    val HYPHACOTTA = register("hyphacotta") { Block(copy(Blocks.TERRACOTTA)) }
    val MYCOVASTUS_HYPHA by BLOCKS.register("mycovastus_hypha", ::MycovastusHyphaBlock)
    val MYCOVASTUS_HYPHA_HIDDEN_BLOCK by BLOCKS.register("mycovastus_hypha_hidden_block", ::MycovastusHyphaHiddenBlock)
    val MYCOVASTUS = register("mycovastus", ::MycovastusBlock)
    val ROTTEN_FUNGUS_HEAP = register("rotten_fungus_heap", ::RottenFungusHeapBlock)
    val TUMIDUSIO_HYPHA by BLOCKS.register("tumidusio_hypha", ::TumidusioHyphaBlock)
    val TUMIDUSIO_HYPHA_HIDDEN_BLOCK by BLOCKS.register("tumidusio_hypha_hidden_block", ::TumidusioHyphaHiddenBlock)
    val HYPHACOAL_BLOCK = register("hyphacoal_block") { Block(copy(Blocks.COAL_BLOCK)) }
    val TUMIDUSIO = register("tumidusio", ::TumidusioBlock)
    val GRANDISPORIA_STIPE by BLOCKS.register("grandisporia_stipe", ::GrandisporiaStipeBlock)
    val GRANDISPORIA_SMALL_CAP by BLOCKS.register("grandisporia_small_cap", ::GrandisporiaSmallCapBlock)
    val GRANDISPORIA_CAP_CENTER by BLOCKS.register("grandisporia_cap_center", ::GrandisporiaCapCenterBlock)
    val GRANDISPORIA_CAP by BLOCKS.register("grandisporia_cap", ::GrandisporiaCapBlock)
    val GRANDISPORIA_WITHERED_CAP = register("grandisporia_withered_cap", ::GrandisporiaWitheredCapBlock)
    val GRANDISPORIA_WITHERED_STIPE = register("grandisporia_withered_stipe") { Block(copy(Blocks.OAK_LOG)) }

    @SubscribeEvent
    fun onRegisterColorHandler(event: RegisterColorHandlersEvent.Block) {
        val hyphaColor = BlockColor { _, _, _, _ -> 0x34b169 }
        event.register(hyphaColor, XENOLICHEN_BLOCK)
        event.register(hyphaColor, MYCOVASTUS_HYPHA)
        event.register(hyphaColor, TUMIDUSIO_HYPHA)
    }
}