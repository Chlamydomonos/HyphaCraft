package xyz.chlamydomonos.hyphacraft.fluids

import com.mojang.blaze3d.shaders.FogShape
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.FogRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BucketItem
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.FlowingFluid
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidType
import org.joml.Vector3f
import xyz.chlamydomonos.hyphacraft.utils.ColorUtil
import java.util.function.Consumer
import java.util.function.Supplier

open class BaseFluidType(
    val name: String,
    val properties: FluidType.Properties,
    val blockProperties: BlockBehaviour.Properties,
    val slopeFindDistance: Int,
    val levelDecreasePerBlock: Int,
    val stillTextureLocation: ResourceLocation,
    val flowingTextureLocation: ResourceLocation,
    val overlayTextureLocation: ResourceLocation,
    tintColorRGBA: Long,
    fogColorRGB: Int,
    val fogStart: Float,
    val fogEnd: Float,
    val customSource: (Supplier<out FlowingFluid>)? = null,
    val customFlowing: (Supplier<out FlowingFluid>)? = null,
    val customBlock: (Supplier<out LiquidBlock>)? = null,
    val customBucket: (Supplier<out BucketItem>)? = null,
) {
    val tintColorInt = ColorUtil.rgba(tintColorRGBA)
    val fogColor = Vector3f(
        ((fogColorRGB shl 16) and 0xff) / 255.0f,
        ((fogColorRGB shl 8) and 0xff) / 255.0f,
        (fogColorRGB and 0xff) / 255.0f
    )

    fun build(): FluidType {
        return object : FluidType(properties) {
            override fun initializeClient(consumer: Consumer<IClientFluidTypeExtensions>) {
                consumer.accept(object : IClientFluidTypeExtensions {
                    override fun getStillTexture() = stillTextureLocation
                    override fun getFlowingTexture() = flowingTextureLocation
                    override fun getOverlayTexture() = overlayTextureLocation
                    override fun getTintColor() = tintColorInt
                    override fun modifyFogColor(
                        camera: Camera,
                        partialTick: Float,
                        level: ClientLevel,
                        renderDistance: Int,
                        darkenWorldAmount: Float,
                        fluidFogColor: Vector3f
                    ) = fogColor

                    override fun modifyFogRender(
                        camera: Camera,
                        mode: FogRenderer.FogMode,
                        renderDistance: Float,
                        partialTick: Float,
                        nearDistance: Float,
                        farDistance: Float,
                        shape: FogShape
                    ) {
                        RenderSystem.setShaderFogStart(fogStart)
                        RenderSystem.setShaderFogEnd(fogEnd)
                    }
                })
            }
        }
    }
}