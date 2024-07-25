package xyz.chlamydomonos.hyphacraft.utils

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation

object NameUtil {
    fun validateBlockName(name: Any?): Boolean {
        return name is String && BuiltInRegistries.BLOCK.containsKey(ResourceLocation.parse(name))
    }

    fun validateTagName(name: Any?): Boolean {
        return name is String && ResourceLocation.tryParse(name) != null
    }
}