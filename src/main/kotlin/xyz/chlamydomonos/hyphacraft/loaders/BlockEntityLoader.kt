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
import xyz.chlamydomonos.hyphacraft.blockentities.MycovastusHyphaBlockEntity
import xyz.chlamydomonos.hyphacraft.blockentities.XenolichenBlockEntity

object BlockEntityLoader {
    val BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, HyphaCraft.MODID)

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

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun <T : BlockEntity> register(
        name: String,
        blockEntity: (BlockPos, BlockState) -> T,
        block: BlockLoader.BlockAndItsItem<*>
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> {
        return BLOCK_ENTITIES.register(name) { ->
            BlockEntityType.Builder.of(blockEntity, block.block).build(null)
        }
    }

    fun register(bus: IEventBus) {
        BLOCK_ENTITIES.register(bus)
    }

    val XENOLICHEN by register("xenolichen", ::XenolichenBlockEntity) { BlockLoader.XENOLICHEN_BLOCK }
    val MYCOVASTUS_HYPHA by register("mycovastus_hypha", ::MycovastusHyphaBlockEntity) { BlockLoader.MYCOVASTUS_HYPHA }
}