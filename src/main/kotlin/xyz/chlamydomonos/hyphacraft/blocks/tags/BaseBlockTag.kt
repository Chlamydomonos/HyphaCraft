package xyz.chlamydomonos.hyphacraft.blocks.tags

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

abstract class BaseBlockTag(val key: TagKey<Block>) {
    companion object {
        data class BlockOrTag(
            val block: Block?,
            val tag: TagKey<Block>?,
            val isBlock: Boolean
        )

        fun block(block: Block): BlockOrTag {
            return BlockOrTag(block, null, true)
        }

        fun tag(tag: TagKey<Block>): BlockOrTag {
            return BlockOrTag(null, tag, false)
        }
    }

    constructor(name: String) : this(TagKey.create(Registries.BLOCK, NameUtil.getRL(name)))

    abstract fun blocks(): Iterable<BlockOrTag>
}