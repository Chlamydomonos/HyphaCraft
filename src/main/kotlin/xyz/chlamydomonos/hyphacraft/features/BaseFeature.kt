package xyz.chlamydomonos.hyphacraft.features

import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.neoforged.neoforge.registries.NeoForgeRegistries
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

abstract class BaseFeature<T : FeatureConfiguration>(
    name: String,
    val feature: Feature<T>
) {
    val featureKey = ResourceKey.create(Registries.CONFIGURED_FEATURE, NameUtil.getRL(name))
    val placementKey = ResourceKey.create(Registries.PLACED_FEATURE, NameUtil.getRL(name))
    val modificationKey = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, NameUtil.getRL("add_$name"))

    abstract fun buildFeature(): T

    abstract fun buildPlacement(feature: Holder<ConfiguredFeature<T, *>>): List<PlacementModifier>

    abstract fun buildModifier(
        biomes: HolderGetter<Biome>,
        consumer: BuildModifierConsumer
    )
}

typealias BuildModifierConsumer = (HolderSet<Biome>, GenerationStep.Decoration) -> Unit