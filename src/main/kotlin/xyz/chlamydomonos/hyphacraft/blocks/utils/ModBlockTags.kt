package xyz.chlamydomonos.hyphacraft.blocks.utils

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

object ModBlockTags {
    val PRESERVE_LEAVES = TagKey.create(Registries.BLOCK, NameUtil.getRL("preserve_leaves"))
    val HYPHA_TREE = TagKey.create(Registries.BLOCK, NameUtil.getRL("hypha_tree"))
}