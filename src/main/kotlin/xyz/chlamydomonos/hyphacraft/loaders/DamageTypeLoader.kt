package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.level.Level
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

object DamageTypeLoader {
    class TypeToRegister(
        val key: ResourceKey<DamageType>,
        val builder: () -> DamageType
    )

    private val DAMAGE_TYPES = arrayListOf<TypeToRegister>()

    fun bootstrap(context: BootstrapContext<DamageType>) {
        for (type in DAMAGE_TYPES) {
            context.register(type.key, type.builder())
        }
    }

    private fun register(name: String, builder: (String) -> DamageType): (Level) -> Holder<DamageType> {
        val key = ResourceKey.create(Registries.DAMAGE_TYPE, NameUtil.getRL(name))
        DAMAGE_TYPES.add(TypeToRegister(key) { builder("${HyphaCraft.MODID}.$name") })
        return { it.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key) }
    }

    val ALIEN_EXPLOSION = register("alien_explosion") { DamageType(it, 0.0f) }
    val HYPHA_EXPLOSION = register("hypha_explosion") { DamageType(it, 0.0f) }
    val DIGESTION = register("digestion") { DamageType(it, 0.0f) }
}