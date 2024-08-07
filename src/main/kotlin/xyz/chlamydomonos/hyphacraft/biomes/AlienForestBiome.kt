package xyz.chlamydomonos.hyphacraft.biomes

import net.minecraft.core.HolderGetter
import net.minecraft.world.level.biome.*
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver
import net.minecraft.world.level.levelgen.placement.PlacedFeature

object AlienForestBiome {
    fun create(
        placedFeatures: HolderGetter<PlacedFeature>,
        worldCarvers: HolderGetter<ConfiguredWorldCarver<*>>
    ): Biome {
        val mobSpawnSettingBuilder = MobSpawnSettings.Builder()
        val biomeGenerationSettingBuilder = BiomeGenerationSettings.Builder(placedFeatures, worldCarvers)
        val biomeSpecialEffectsBuilder = BiomeSpecialEffects.Builder()
            .waterColor(0xbd8ad7)
            .waterFogColor(0xeab2ff)
            .fogColor(0xc0c9ff)
            .skyColor(0xb574e3)
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)

        return Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(1.0f)
            .downfall(1.0f)
            .specialEffects(biomeSpecialEffectsBuilder.build())
            .mobSpawnSettings(mobSpawnSettingBuilder.build())
            .generationSettings(biomeGenerationSettingBuilder.build())
            .build()
    }
}