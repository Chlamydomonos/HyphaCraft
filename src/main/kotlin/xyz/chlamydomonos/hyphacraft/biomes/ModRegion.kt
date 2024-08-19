package xyz.chlamydomonos.hyphacraft.biomes

import com.mojang.datafixers.util.Pair
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.Climate
import terrablender.api.Region
import terrablender.api.RegionType
import xyz.chlamydomonos.hyphacraft.loaders.BiomeLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil
import java.lang.reflect.Modifier
import java.util.function.Consumer

class ModRegion : Region(NameUtil.getRL("overworld"), RegionType.OVERWORLD, 5) {
    @Suppress("UNCHECKED_CAST")
    override fun addBiomes(
        registry: Registry<Biome>?,
        mapper: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>?
    ) {
        addModifiedVanillaOverworldBiomes(mapper) {
            val biomesClass = Biomes::class.java
            val resourceKeyClass = ResourceKey::class.java
            val staticFields = biomesClass.declaredFields.filter { field ->
                Modifier.isStatic(field.modifiers) && resourceKeyClass.isAssignableFrom(field.type)
            }
            for (field in staticFields) {
                it.replaceBiome(field.get(null) as ResourceKey<Biome>, BiomeLoader.ANCIENT_ALIEN_FOREST.key)
            }
        }
    }
}