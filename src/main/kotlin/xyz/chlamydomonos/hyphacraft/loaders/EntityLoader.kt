package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.entity.entities.HumifossorEntity
import xyz.chlamydomonos.hyphacraft.entity.entities.SporeCloudEntity
import xyz.chlamydomonos.hyphacraft.entity.entities.TerraborerBombEntity
import xyz.chlamydomonos.hyphacraft.entity.entities.TransportEntity

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
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
    val TRANSPORT by register("transport", TransportEntity.BUILDER)
    val SPORE_CLOUD by register("spore_cloud", SporeCloudEntity.BUILDER)
    val HUMIFOSSOR by register("humifossor", HumifossorEntity.BUILDER)

    @SubscribeEvent
    fun onAttributesSetup(event: EntityAttributeCreationEvent) {
        event.put(HUMIFOSSOR, HumifossorEntity.mobBuilder().build())
    }
}