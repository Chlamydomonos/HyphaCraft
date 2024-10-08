package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.blockentities.*

object BlockEntityLoader {
    private val BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, HyphaCraft.MODID)

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun <T : BlockEntity> register(
        name: String,
        blockEntity: (BlockPos, BlockState) -> T,
        block: () -> Block
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> {
        return BLOCK_ENTITIES.register(name) { ->
            BlockEntityType.Builder.of(blockEntity, block()).build(null)
        }
    }

    fun register(bus: IEventBus) {
        BLOCK_ENTITIES.register(bus)
    }

    val XENOLICHEN by register("xenolichen", ::XenolichenBlockEntity) { BlockLoader.XENOLICHEN_BLOCK }
    val MYCOVASTUS_HYPHA by register("mycovastus_hypha", ::MycovastusHyphaBlockEntity) { BlockLoader.MYCOVASTUS_HYPHA }
    val TUMIDUSIO_HYPHA by register("tumidusio_hypha", ::TumidusioHyphaBlockEntity) { BlockLoader.TUMIDUSIO_HYPHA }
    val CARNIVORAVITIS_VINE by register("carnivoravitis_vine", ::CarnivoravitisVineBlockEntity) { BlockLoader.CARNIVORAVITIS_VINE }
    val CARNIVORAVITIS_FLOWER by register("carnivoravitis_flower", ::CarnivoravitisFlowerBlockEntity) { BlockLoader.CARNIVORAVITIS_FLOWER }
    val PULVERIUM by register("pulverium", ::PulveriumBlockEntity) { BlockLoader.PULVERIUM.block }
    val FULGURFUNGUS by register("fulgurfungus", ::FulgurfungusBlockEntity) { BlockLoader.FULGURFUNGUS.block }
}