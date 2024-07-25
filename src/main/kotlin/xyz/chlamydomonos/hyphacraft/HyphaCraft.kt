package xyz.chlamydomonos.hyphacraft

import net.neoforged.fml.ModLoadingContext
import net.neoforged.fml.common.Mod
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import xyz.chlamydomonos.hyphacraft.loaders.*

@Mod(HyphaCraft.MODID)
object HyphaCraft {
    const val MODID = "hyphacraft"

    init {
        ConfigLoader.register(ModLoadingContext.get())
        ItemLoader.register(MOD_BUS)
        CreativeTabLoader.register(MOD_BUS)
        BlockLoader.register(MOD_BUS)
        BlockEntityLoader.register(MOD_BUS)
    }
}