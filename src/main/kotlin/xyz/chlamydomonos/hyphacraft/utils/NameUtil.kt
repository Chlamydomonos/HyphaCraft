package xyz.chlamydomonos.hyphacraft.utils

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import xyz.chlamydomonos.hyphacraft.HyphaCraft

object NameUtil {
    fun validateBlockName(name: Any?): Boolean {
        return name is String && BuiltInRegistries.BLOCK.containsKey(ResourceLocation.parse(name))
    }

    fun validateTagName(name: Any?): Boolean {
        return name is String && ResourceLocation.tryParse(name) != null
    }

    fun getRL(path: String) = ResourceLocation.fromNamespaceAndPath(HyphaCraft.MODID, path)

    fun path(block: Block) = BuiltInRegistries.BLOCK.getKey(block).path

    fun path(item: Item) = BuiltInRegistries.ITEM.getKey(item).path

    fun getEntityTexture(name: String) = getRL("textures/entity/$name.png")
}