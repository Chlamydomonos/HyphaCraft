package xyz.chlamydomonos.hyphacraft.biomes

import net.minecraft.core.HolderGetter
import net.minecraft.world.level.biome.*
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver
import net.minecraft.world.level.levelgen.placement.PlacedFeature

object AncientAlienForestBiome {
    fun create(
        placedFeatures: HolderGetter<PlacedFeature>,
        worldCarvers: HolderGetter<ConfiguredWorldCarver<*>>
    ): Biome {
        val mobSpawnSettingBuilder = MobSpawnSettings.Builder()
        val biomeGenerationSettingBuilder = BiomeGenerationSettings.Builder(placedFeatures, worldCarvers)
        val biomeSpecialEffectsBuilder = BiomeSpecialEffects.Builder()
            .waterColor(0xa5adc4)
            .waterFogColor(0xa5bbc4)
            .fogColor(0xa5bbc4)
            .skyColor(0xb2a5c4)
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)

        return Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.25f)
            .downfall(0.2f)
            .specialEffects(biomeSpecialEffectsBuilder.build())
            .mobSpawnSettings(mobSpawnSettingBuilder.build())
            .generationSettings(biomeGenerationSettingBuilder.build())
            .build()
    }
}