package xyz.chlamydomonos.hyphacraft.datacomponents

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block

class BlockHolder(val block: Block) {
    constructor(name: String) : this(BuiltInRegistries.BLOCK.get(ResourceLocation.parse(name)))

    companion object {
        val CODEC = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.fieldOf("blockName").forGetter(BlockHolder::blockName)
            ).apply(instance, ::BlockHolder)
        }

        val STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, BlockHolder::blockName,
            ::BlockHolder
        )
    }

    val blockName get() = BuiltInRegistries.BLOCK.getKey(block).toString()

    override fun hashCode() = block.hashCode()

    override fun equals(other: Any?) = other is BlockHolder && other.block == block
}