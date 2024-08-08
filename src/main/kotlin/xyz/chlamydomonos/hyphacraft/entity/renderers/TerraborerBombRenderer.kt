package xyz.chlamydomonos.hyphacraft.entity.renderers

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import xyz.chlamydomonos.hyphacraft.entity.entities.TerraborerBombEntity
import xyz.chlamydomonos.hyphacraft.entity.models.TerraborerBombModel
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class TerraborerBombRenderer(
    context: EntityRendererProvider.Context
) : EntityRenderer<TerraborerBombEntity>(context) {
    val model = TerraborerBombModel(context.bakeLayer(TerraborerBombModel.LAYER_LOCATION))

    override fun getTextureLocation(entity: TerraborerBombEntity) = NameUtil.getEntityTexture("terraborer_bomb")

    override fun render(
        entity: TerraborerBombEntity,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int
    ) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)
        poseStack.pushPose()
        val buffer = bufferSource.getBuffer(model.renderType(getTextureLocation(entity)))
        model.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, -1)
        poseStack.popPose()
    }
}