package xyz.chlamydomonos.hyphacraft.entity.renderers

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.model.DefaultedEntityGeoModel
import software.bernie.geckolib.renderer.GeoEntityRenderer
import software.bernie.geckolib.renderer.layer.GeoRenderLayer
import xyz.chlamydomonos.hyphacraft.entity.entities.HumifossorEntity
import xyz.chlamydomonos.hyphacraft.utils.ColorUtil
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class HumifossorRenderer(renderManager: EntityRendererProvider.Context?) : GeoEntityRenderer<HumifossorEntity>(
    renderManager,
    DefaultedEntityGeoModel(NameUtil.getRL("humifossor"))
) {
    companion object {
        val CHARGE_TEXTURE = NameUtil.getEntityTexture("humifossor_charge")
    }

    init {
        addRenderLayer(object : GeoRenderLayer<HumifossorEntity>(this) {
            override fun render(
                poseStack: PoseStack?,
                animatable: HumifossorEntity?,
                bakedModel: BakedGeoModel?,
                renderType: RenderType?,
                bufferSource: MultiBufferSource?,
                buffer: VertexConsumer?,
                partialTick: Float,
                packedLight: Int,
                packedOverlay: Int
            ) {
                if (animatable == null || bufferSource == null) {
                    return
                }

                if (!animatable.charged) {
                    return
                }

                val f = animatable.tickCount + partialTick

                poseStack?.pushPose()
                poseStack?.scale(1.1f, 1.1f, 1.1f)
                val realRenderType = RenderType.energySwirl(CHARGE_TEXTURE, f * 0.01f % 1.0f, f * 0.01f % 1.0f)
                val realBuffer = bufferSource.getBuffer(realRenderType)
                renderer.reRender(
                    bakedModel,
                    poseStack,
                    bufferSource,
                    animatable,
                    realRenderType,
                    realBuffer,
                    partialTick,
                    packedLight,
                    packedOverlay,
                    ColorUtil.rgba(0x808080ff)
                    )
                poseStack?.popPose()
            }
        })
    }
}