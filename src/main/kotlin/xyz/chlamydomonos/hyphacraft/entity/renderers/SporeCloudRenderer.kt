package xyz.chlamydomonos.hyphacraft.entity.renderers

import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import xyz.chlamydomonos.hyphacraft.entity.entities.SporeCloudEntity
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class SporeCloudRenderer(context: EntityRendererProvider.Context) : EntityRenderer<SporeCloudEntity>(context) {
    override fun getTextureLocation(entity: SporeCloudEntity) = NameUtil.getEntityTexture("empty")
}