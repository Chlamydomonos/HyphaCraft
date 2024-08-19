package xyz.chlamydomonos.hyphacraft.features

import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer
import net.minecraft.world.level.levelgen.placement.*
import xyz.chlamydomonos.hyphacraft.loaders.BiomeLoader
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class HyphacoalTreeFeature : BaseFeature<TreeConfiguration>("hyphacoal_tree", Feature.TREE) {
    override fun buildFeature() = TreeConfiguration.TreeConfigurationBuilder(
        BlockStateProvider.simple(BlockLoader.HYPHACOAL_BLOCK.block),
        ForkingTrunkPlacer(8, 8, 8),
        BlockStateProvider.simple(Blocks.AIR),
        RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 0),
        TwoLayersFeatureSize(1, 0, 1)
    ).dirt(BlockStateProvider.simple(BlockLoader.HYPHACOTTA.block)).build()

    override fun buildModifier(biomes: HolderGetter<Biome>, consumer: BuildModifierConsumer) {
        consumer(
            HolderSet.direct(biomes.getOrThrow(BiomeLoader.ANCIENT_ALIEN_FOREST.key)),
            GenerationStep.Decoration.VEGETAL_DECORATION
        )
    }

    override fun buildPlacement(feature: Holder<ConfiguredFeature<TreeConfiguration, *>>) = listOf(
        CountPlacement.of(16),
        InSquarePlacement.spread(),
        SurfaceWaterDepthFilter.forMaxDepth(0),
        PlacementUtils.HEIGHTMAP,
        BlockPredicateFilter.forPredicate(
            BlockPredicate.matchesBlocks(
                Direction.DOWN.normal,
                BlockLoader.HYPHACOTTA.block
            )
        ),
        BiomeFilter.biome()
    )
}