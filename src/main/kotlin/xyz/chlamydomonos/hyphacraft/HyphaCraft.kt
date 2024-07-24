package xyz.chlamydomonos.hyphacraft

import net.neoforged.fml.common.Mod
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.CreativeTabLoader
import xyz.chlamydomonos.hyphacraft.loaders.ItemLoader

@Mod(HyphaCraft.MODID)
object HyphaCraft {
    const val MODID = "hyphacraft"

    init {
        ItemLoader.register(MOD_BUS)
        CreativeTabLoader.register(MOD_BUS)
        BlockLoader.register(MOD_BUS)
    }
}