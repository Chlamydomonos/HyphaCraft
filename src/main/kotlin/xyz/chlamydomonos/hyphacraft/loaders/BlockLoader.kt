package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.client.color.block.BlockColor
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
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
    val BLOCKS_WITH_LOOT = arrayListOf<DeferredHolder<Block, out Block>>()

    private fun <T : Block> register(name: String, block: () -> T): BlockAndItsItem<T> {
        return register(name, 0, block)
    }

    private fun <T : Block> register(name: String, priority: Int, block: () -> T): BlockAndItsItem<T> {
        val registeredBlock = blockOnly(name, block)
        BLOCKS_WITH_LOOT.add(registeredBlock)
        val registeredItem = ItemLoader.register(name, priority) { BlockItem(registeredBlock.get(), Item.Properties()) }
        return BlockAndItsItem(registeredBlock, registeredItem)
    }
    
    private fun <T : Block> blockOnly(name: String, block: () -> T): DeferredHolder<Block, T> {
        val registeredBlock = BLOCKS.register(name, block)
        BLOCKS_WITH_LOOT.add(registeredBlock)
        return registeredBlock
    }

    fun register(bus: IEventBus) {
        BLOCKS.register(bus)
    }

    private fun copy(block: Block) = BlockBehaviour.Properties.ofFullCopy(block)

    val TEST_BLOCK by blockOnly("test_block", ::TestBlock)
    val ALIEN_ROCK = register("alien_rock") { Block(copy(Blocks.STONE).mapColor(MapColor.DEEPSLATE)) }
    val ALIEN_SOIL = register("alien_soil", ::AlienSoilBlock)
    val XENOLICHEN_BLOCK by blockOnly("xenolichen_block", ::XenolichenBlock)
    val XENOLICHEN_HIDDEN_BLOCK by blockOnly("xenolichen_hidden_block", ::BlockCopierHiddenBlock)
    val HYPHACOTTA = register("hyphacotta") { HyphaResidueBlock(copy(Blocks.TERRACOTTA).mapColor(MapColor.COLOR_GRAY)) }
    val MYCOVASTUS_HYPHA by blockOnly("mycovastus_hypha", ::MycovastusHyphaBlock)
    val MYCOVASTUS_HYPHA_HIDDEN_BLOCK by blockOnly("mycovastus_hypha_hidden_block", ::BlockCopierHiddenBlock)
    val MYCOVASTUS = register("mycovastus", ::MycovastusBlock)
    val ROTTEN_FUNGUS_HEAP = register("rotten_fungus_heap", ::RottenFungusHeapBlock)
    val TUMIDUSIO_HYPHA by blockOnly("tumidusio_hypha", ::TumidusioHyphaBlock)
    val TUMIDUSIO_HYPHA_HIDDEN_BLOCK by blockOnly("tumidusio_hypha_hidden_block", ::BlockCopierHiddenBlock)
    val HYPHACOAL_BLOCK = register("hyphacoal_block") { HyphaResidueBlock(copy(Blocks.COAL_BLOCK).mapColor(MapColor.COLOR_BLACK)) }
    val TUMIDUSIO = register("tumidusio", ::TumidusioBlock)
    val GRANDISPORIA_STIPE by blockOnly("grandisporia_stipe", ::GrandisporiaStipeBlock)
    val GRANDISPORIA_SMALL_CAP by blockOnly("grandisporia_small_cap", ::GrandisporiaSmallCapBlock)
    val GRANDISPORIA_CAP_CENTER by blockOnly("grandisporia_cap_center", ::GrandisporiaCapCenterBlock)
    val GRANDISPORIA_CAP by blockOnly("grandisporia_cap", ::GrandisporiaCapBlock)
    val GRANDISPORIA_WITHERED_CAP = register("grandisporia_withered_cap", ::GrandisporiaWitheredCapBlock)
    val GRANDISPORIA_WITHERED_STIPE = register("grandisporia_withered_stipe", ::GrandisporiaWitheredStipeBlock)
    val SPORE_HEAP = register("spore_heap", ::SporeHeapBlock)
    val HUMUS_HEAP = register("humus_heap", ::HumusHeapBlock)
    val ALIEN_EXPLOSIVE by blockOnly("alien_explosive", ::AlienExplosiveBlock)
    val TERRABORER_STIPE = register("terraborer_stipe", ::TerraborerStipeBlock)
    val TERRABORER_BOMB = register("terraborer_bomb", ::TerraborerBombBlock)
    val ACTIVE_HYPHA_BLOCK = register("active_hypha_block", ::ActiveHyphaBlock)
    val LOOSE_FUNGUS_ROOT by blockOnly("loose_fungus_root", ::LooseFungusRootBlock)
    val CARNIVORAVITIS_VINE by blockOnly("carnivoravitis_vine", ::CarnivoravitisVineBlock)
    val CARNIVORAVITIS_ROOT by blockOnly("carnivoravitis_root", ::CarnivoravitisRootBlock)
    val CARNIVORAVITIS_SHELL = register("carnivoravitis_shell", ::CarnivoravitisShellBlock)
    val CARNIVORAVITIS_FLOWER by blockOnly("carnivoravitis_flower", ::CarnivoravitisFlowerBlock)
    val DIGESTIVE_JUICE_BLOCK by blockOnly("digestive_juice_block", ::DigestiveJuiceBlock)
    val CARNIVORAVITIS_SEEDLING by blockOnly("carnivoravitis_seedling", ::CarnivoravitisSeedlingBlock)
    val VERMILINGUA by blockOnly("vermilingua", ::VermilinguaBlock)
    val ALIEN_SWARD = register("alien_sward", ::AlienSwardBlock)
    val FERTILE_ALIEN_SWARD = register("fertile_alien_sward", ::FertileAlienSwardBlock)
    val PULVERIUM = register("pulverium", ::PulveriumBlock)
    val HARDENED_FUNGUS_SHELL = register("hardened_fungus_shell", ::HardenedFungusShellBlock)
    val ROTTEN_GOO_BLOCK by blockOnly("rotten_goo_block", ::RottenGooBlock)
    val FULGURFUNGUS = register("fulgurfungus", ::FulgurfungusBlock)

    @SubscribeEvent
    fun onRegisterColorHandler(event: RegisterColorHandlersEvent.Block) {
        val hyphaColor = BlockColor { _, _, _, _ -> 0x34b169 }
        event.register(hyphaColor, XENOLICHEN_BLOCK)
        event.register(hyphaColor, MYCOVASTUS_HYPHA)
        event.register(hyphaColor, TUMIDUSIO_HYPHA)
    }
}