package xyz.chlamydomonos.hyphacraft.blocks.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

abstract class BaseBlockTag(val key: TagKey<Block>) {
    constructor(name: String) : this(TagKey.create(Registries.BLOCK, NameUtil.getRL(name)))

    abstract fun blocks(): Iterable<Block>
}