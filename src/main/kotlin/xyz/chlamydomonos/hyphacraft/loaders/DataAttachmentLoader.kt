package xyz.chlamydomonos.hyphacraft.loaders

import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.chlamydomonos.hyphacraft.HyphaCraft

object DataAttachmentLoader {
    private val ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, HyphaCraft.MODID)

    fun register(bus: IEventBus) {
        ATTACHMENT_TYPES.register(bus)
    }

    fun <T> register(
        name: String,
        builder: () -> AttachmentType<T>
    ): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
        return ATTACHMENT_TYPES.register(name, builder)
    }

    val SPORE_HEAP_COUNT by register("spore_heap_count") { AttachmentType.builder { -> 0 }.build() }
    val IS_ALIEN_FOREST by register("is_alien_forest") { AttachmentType.builder { -> false }.build() }
}