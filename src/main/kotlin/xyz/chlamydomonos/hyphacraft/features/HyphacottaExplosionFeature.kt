package xyz.chlamydomonos.hyphacraft.features

import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest
import xyz.chlamydomonos.hyphacraft.loaders.BiomeLoader
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class HyphacottaExplosionFeature :BaseFeature<OreConfiguration>("hyphacotta_explosion", Feature.ORE) {
    override fun buildFeature() = OreConfiguration(
        listOf(OreConfiguration.target(
            BlockMatchTest(BlockLoader.HYPHACOTTA.block),
            Blocks.AIR.defaultBlockState()
        )),
        64
    )

    override fun buildModifier(biomes: HolderGetter<Biome>, consumer: BuildModifierConsumer) {
        consumer(
            HolderSet.direct(biomes.getOrThrow(BiomeLoader.ANCIENT_ALIEN_FOREST.key)),
            GenerationStep.Decoration.UNDERGROUND_ORES
        )
    }

    override fun buildPlacement(feature: Holder<ConfiguredFeature<OreConfiguration, *>>) = listOf(
        CountPlacement.of(256),
        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top())
    )
}