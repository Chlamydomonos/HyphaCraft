package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.features.FeatureUtils
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers
import net.neoforged.neoforge.registries.NeoForgeRegistries
import xyz.chlamydomonos.hyphacraft.features.BaseFeature
import xyz.chlamydomonos.hyphacraft.features.HyphacoalTreeFeature
import xyz.chlamydomonos.hyphacraft.features.HyphacottaExplosionFeature

object FeatureLoader {
    class LoadedFeature<T : FeatureConfiguration>(feature: BaseFeature<T>) {
        val configuredKey = feature.featureKey
        val placedKey = feature.placementKey
        val modifierKey = feature.modificationKey
        fun configured(level: Level) = level.holderLookup(Registries.CONFIGURED_FEATURE).getOrThrow(configuredKey)
        fun placed(level: Level) = level.holderLookup(Registries.PLACED_FEATURE).getOrThrow(placedKey)
        fun modifier(level: Level) = level.holderLookup(NeoForgeRegistries.Keys.BIOME_MODIFIERS).getOrThrow(modifierKey)
    }

    private val FEATURES = arrayListOf<BaseFeature<*>>()
    fun <T : FeatureConfiguration> register(feature: BaseFeature<T>): LoadedFeature<T> {
        FEATURES.add(feature)
        return LoadedFeature(feature)
    }

    private fun <T : FeatureConfiguration> registerConfigured(
        context: BootstrapContext<ConfiguredFeature<*, *>>,
        feature: BaseFeature<T>
    ) {
        FeatureUtils.register(context, feature.featureKey, feature.feature, feature.buildFeature())
    }

    fun bootstrapConfiguredFeature(context: BootstrapContext<ConfiguredFeature<*, *>>) {
        for (feature in FEATURES) {
            registerConfigured(context, feature)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : FeatureConfiguration> registerPlaced(
        context: BootstrapContext<PlacedFeature>,
        feature: BaseFeature<T>
    ) {
        val holder = context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(feature.featureKey)
        val placement = feature.buildPlacement(holder as Holder<ConfiguredFeature<T, *>>)
        PlacementUtils.register(context, feature.placementKey, holder, placement)
    }

    fun bootstrapPlacedFeature(context: BootstrapContext<PlacedFeature>) {
        for (feature in FEATURES) {
            registerPlaced(context, feature)
        }
    }

    fun bootstrapBiomeModifier(context: BootstrapContext<BiomeModifier>) {
        for (feature in FEATURES) {
            val biomes = context.lookup(Registries.BIOME)
            val holder = context.lookup(Registries.PLACED_FEATURE).getOrThrow(feature.placementKey)
            var outputBiomes: HolderSet<Biome>? = null
            var outputStep: GenerationStep.Decoration? = null
            feature.buildModifier(biomes) { biomeSet, step ->
                outputBiomes = biomeSet
                outputStep = step
            }

            if (outputBiomes == null || outputStep == null) {
                return
            }

            context.register(feature.modificationKey, BiomeModifiers.AddFeaturesBiomeModifier(
                outputBiomes!!,
                HolderSet.direct(holder),
                outputStep!!
            ))
        }
    }

    val HYPHACOTTA_EXPLOSION = register(HyphacottaExplosionFeature())
    val HYPHACOAL_TREE = register(HyphacoalTreeFeature())
}