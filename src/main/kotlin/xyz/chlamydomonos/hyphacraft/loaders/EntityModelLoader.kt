package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.client.renderer.entity.EntityRenderers
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.entity.models.TerraborerBombModel
import xyz.chlamydomonos.hyphacraft.entity.renderers.TerraborerBombRenderer
import xyz.chlamydomonos.hyphacraft.entity.renderers.TransportRenderer

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
object EntityModelLoader {
    @SubscribeEvent
    fun registerEntityLayers(event: EntityRenderersEvent.RegisterLayerDefinitions) {
        event.registerLayerDefinition(TerraborerBombModel.LAYER_LOCATION, TerraborerBombModel::createBodyLayer)
    }

    @SubscribeEvent
    fun registerEntityRenderers(event: FMLClientSetupEvent) {
        event.enqueueWork {
            EntityRenderers.register(EntityLoader.TERRABORER_BOMB, ::TerraborerBombRenderer)
            EntityRenderers.register(EntityLoader.TRANSPORT, ::TransportRenderer)
        }
    }
}