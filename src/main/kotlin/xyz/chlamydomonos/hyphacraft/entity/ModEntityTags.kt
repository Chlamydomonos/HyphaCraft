package xyz.chlamydomonos.hyphacraft.entity

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

object ModEntityTags {
    val HYPHACRAFT_INSECT = TagKey.create(Registries.ENTITY_TYPE, NameUtil.getRL("hyphacraft_insect"))
}