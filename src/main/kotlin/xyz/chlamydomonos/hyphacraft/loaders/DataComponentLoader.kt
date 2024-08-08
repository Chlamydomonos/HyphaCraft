package xyz.chlamydomonos.hyphacraft.loaders

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.StreamCodec
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.datacomponents.BlockHolder

object DataComponentLoader {
    private val DATA_COMPONENTS = DeferredRegister.createDataComponents(HyphaCraft.MODID)

    fun register(bus: IEventBus) {
        DATA_COMPONENTS.register(bus)
    }

    fun <T> register(
        name: String,
        codec: Codec<T>,
        streamCodec: StreamCodec<ByteBuf, T>
    ): DeferredHolder<DataComponentType<*>, DataComponentType<T>> {
        return DATA_COMPONENTS.registerComponentType(name) { it.persistent(codec).networkSynchronized(streamCodec) }
    }

    val BLOCK_HOLDER by register("block_holder", BlockHolder.CODEC, BlockHolder.STREAM_CODEC)
}