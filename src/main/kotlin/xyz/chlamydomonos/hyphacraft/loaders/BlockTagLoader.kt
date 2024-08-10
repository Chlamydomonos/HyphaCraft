package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import xyz.chlamydomonos.hyphacraft.blocks.tags.BaseBlockTag
import xyz.chlamydomonos.hyphacraft.blocks.tags.minecraft.MineableWithAxeTag
import xyz.chlamydomonos.hyphacraft.blocks.tags.minecraft.MineableWithHoeTag
import xyz.chlamydomonos.hyphacraft.blocks.tags.minecraft.MineableWithPickaxeTag
import xyz.chlamydomonos.hyphacraft.blocks.tags.minecraft.MineableWithShovelTag
import xyz.chlamydomonos.hyphacraft.blocks.tags.mod.*

object BlockTagLoader {
    val TAGS = mutableListOf<BaseBlockTag>()

    fun register(builder: () -> BaseBlockTag): TagKey<Block> {
        val tag = builder()
        TAGS.add(tag)
        return tag.key
    }

    init {
        register(::MineableWithPickaxeTag)
        register(::MineableWithShovelTag)
        register(::MineableWithAxeTag)
        register(::MineableWithHoeTag)
    }

    val PRESERVE_LEAVES = register(::PreserveLeavesTag)
    val HYPHA_TREE = register(::HyphaTreeTag)
    val GRANDISPORIA_STIPE_CONNECTABLE = register(::GrandisporiaStipeConnectableTag)
    val CARNIVORAVITIS_VINE_CONNECTABLE = register(::CarnivoravitisVineConnectableTag)
    val CARNIVORAVITIS_PLANT = register(::CarnivoravitisPlantTag)
}