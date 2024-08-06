package xyz.chlamydomonos.hyphacraft.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.loaders.BiomeLoader
import xyz.chlamydomonos.hyphacraft.loaders.DamageTypeLoader
import java.util.concurrent.CompletableFuture

class ModDatapackBuiltinEntriesProvider(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>
) : DatapackBuiltinEntriesProvider(output, registries, BUILDER, setOf(HyphaCraft.MODID)) {
    companion object {
        val BUILDER = RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, DamageTypeLoader::bootstrap)
            .add(Registries.BIOME, BiomeLoader::bootstrap)
    }
}