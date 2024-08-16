package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import xyz.chlamydomonos.hyphacraft.biomes.AlienForestBiome
import xyz.chlamydomonos.hyphacraft.biomes.AncientAlienForestBiome
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

object BiomeLoader {
    class BiomeToRegister(
        val key: ResourceKey<Biome>,
        val builder: (HolderGetter<PlacedFeature>, HolderGetter<ConfiguredWorldCarver<*>>) -> Biome
    )

    private val BIOMES = arrayListOf<BiomeToRegister>()

    fun bootstrap(context: BootstrapContext<Biome>) {
        val placedFeatures = context.lookup(Registries.PLACED_FEATURE)
        val worldCarvers = context.lookup(Registries.CONFIGURED_CARVER)
        for (biome in BIOMES) {
            context.register(biome.key, biome.builder(placedFeatures, worldCarvers))
        }
    }

    private fun register(
        name: String, builder: (HolderGetter<PlacedFeature>, HolderGetter<ConfiguredWorldCarver<*>>) -> Biome
    ): (Level) -> Holder<Biome> {
        val key = ResourceKey.create(Registries.BIOME, NameUtil.getRL(name))
        BIOMES.add(BiomeToRegister(key, builder))
        return { it.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(key) }
    }

    val ALIEN_FOREST = register("alien_forest", AlienForestBiome::create)
    val ANCIENT_ALIEN_FOREST = register("ancient_alien_forest", AncientAlienForestBiome::create)
}