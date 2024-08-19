package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import terrablender.api.Regions
import terrablender.api.SurfaceRuleManager
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.biomes.AlienForestBiome
import xyz.chlamydomonos.hyphacraft.biomes.AncientAlienForestBiome
import xyz.chlamydomonos.hyphacraft.biomes.ModRegion
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
object BiomeLoader {
    class LoadedBiome(
        val key: ResourceKey<Biome>
    ) {
        fun get(level: Level) = level.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(key)
    }

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
    ): LoadedBiome {
        val key = ResourceKey.create(Registries.BIOME, NameUtil.getRL(name))
        BIOMES.add(BiomeToRegister(key, builder))
        return LoadedBiome(key)
    }

    val ALIEN_FOREST = register("alien_forest", AlienForestBiome::create)
    val ANCIENT_ALIEN_FOREST = register("ancient_alien_forest", AncientAlienForestBiome::create)

    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            Regions.register(ModRegion())
            SurfaceRuleManager.addSurfaceRules(
                SurfaceRuleManager.RuleCategory.OVERWORLD,
                HyphaCraft.MODID,
                AncientAlienForestBiome.surfaceRule
            )
        }
    }
}