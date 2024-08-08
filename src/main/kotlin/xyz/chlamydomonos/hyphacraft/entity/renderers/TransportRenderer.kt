package xyz.chlamydomonos.hyphacraft.entity.renderers

import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import xyz.chlamydomonos.hyphacraft.entity.entities.TransportEntity
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class TransportRenderer(context: EntityRendererProvider.Context) : EntityRenderer<TransportEntity>(context) {
    override fun getTextureLocation(entity: TransportEntity) = NameUtil.getEntityTexture("empty")
}