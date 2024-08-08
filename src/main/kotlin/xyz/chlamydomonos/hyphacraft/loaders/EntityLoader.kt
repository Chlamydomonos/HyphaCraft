package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.entity.entities.TerraborerBombEntity

object EntityLoader {
    private val ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, HyphaCraft.MODID)

    fun register(bus: IEventBus) {
        ENTITY_TYPES.register(bus)
    }

    fun <T : Entity> register(
        name: String,
        builder: EntityType.Builder<T>
    ): DeferredHolder<EntityType<*>, EntityType<T>> {
        return ENTITY_TYPES.register(name) { -> builder.build(name) }
    }

    val TERRABORER_BOMB by register("terraborer_bomb", TerraborerBombEntity.BUILDER)
}