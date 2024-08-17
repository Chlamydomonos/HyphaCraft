package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import xyz.chlamydomonos.hyphacraft.blocks.base.ImmuneToHyphaExplosionBlock

class LooseFungusRootBlock : ImmuneToHyphaExplosionBlock(
    Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.FUNGUS).noOcclusion().mapColor(DyeColor.WHITE).ignitedByLava()
)