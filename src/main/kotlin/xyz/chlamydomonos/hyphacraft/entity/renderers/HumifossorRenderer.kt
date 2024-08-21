package xyz.chlamydomonos.hyphacraft.entity.renderers

import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.model.DefaultedEntityGeoModel
import software.bernie.geckolib.renderer.GeoEntityRenderer
import xyz.chlamydomonos.hyphacraft.entity.entities.HumifossorEntity
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class HumifossorRenderer(renderManager: EntityRendererProvider.Context?) : GeoEntityRenderer<HumifossorEntity>(
    renderManager,
    DefaultedEntityGeoModel(NameUtil.getRL("humifossor"))
)