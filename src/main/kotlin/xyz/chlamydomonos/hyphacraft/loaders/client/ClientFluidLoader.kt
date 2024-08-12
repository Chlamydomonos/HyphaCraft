package xyz.chlamydomonos.hyphacraft.loaders.client

import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.loaders.FluidLoader
import xyz.chlamydomonos.hyphacraft.loaders.FluidLoader.RenderTypeForRegistry.*

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ClientFluidLoader {
    private fun getRenderType(type: FluidLoader.RenderTypeForRegistry): RenderType {
        return when (type) {
            SOLID -> RenderType.SOLID
            TRANSLUCENT -> RenderType.TRANSLUCENT
            CUTOUT -> RenderType.CUTOUT
        }
    }

    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        event.enqueueWork {
            for (fluid in FluidLoader.fluidsToRender) {
                ItemBlockRenderTypes.setRenderLayer(fluid.fluidHolder.get(), getRenderType(fluid.renderType))
            }
        }
    }
}