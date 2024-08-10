package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.fluids.BaseFluidType
import xyz.chlamydomonos.hyphacraft.fluids.DigestiveJuiceFluid
import java.util.function.Supplier
import kotlin.reflect.KProperty

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
object FluidLoader {
    class DeferredSupplier<T> : Supplier<T> {
        lateinit var supplier: Supplier<out T>
        override fun get() = supplier.get()
    }

    class LoadedFluid (
        typeHolder: Supplier<FluidType>,
        sourceHolder: Supplier<out FlowingFluid>,
        flowingHolder: Supplier<out FlowingFluid>,
        blockHolder: Supplier<out LiquidBlock>,
        bucketHolder: Supplier<out BucketItem>
    ) {
        val type by typeHolder
        val source by sourceHolder
        val flowing by flowingHolder
        val block by blockHolder
        val bucket by bucketHolder
    }

    data class FluidToRender(
        val fluidHolder: Supplier<out FlowingFluid>,
        val renderType: RenderType
    )

    private val FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, HyphaCraft.MODID)
    private val FLUIDS = DeferredRegister.create(Registries.FLUID, HyphaCraft.MODID)

    fun register(bus: IEventBus) {
        FLUID_TYPES.register(bus)
        FLUIDS.register(bus)
    }

    private val fluidsToRender = arrayListOf<FluidToRender>()

    fun register(
        type: BaseFluidType,
        renderType: RenderType = RenderType.TRANSLUCENT,
        bucketPriority: Int = 0
    ): LoadedFluid {
        val typeHolder = FLUID_TYPES.register(type.name, type::build)

        val sourceSupplier = DeferredSupplier<Fluid>()
        val flowingSupplier = DeferredSupplier<Fluid>()
        val blockSupplier = DeferredSupplier<LiquidBlock>()
        val bucketSupplier = DeferredSupplier<BucketItem>()

        val fluidProperties = BaseFlowingFluid.Properties(typeHolder, sourceSupplier, flowingSupplier)
            .block(blockSupplier)
            .bucket(bucketSupplier)
            .slopeFindDistance(type.slopeFindDistance)
            .levelDecreasePerBlock(type.levelDecreasePerBlock)

        val sourceHolder: Supplier<out FlowingFluid> = type.customSource
            ?: FLUIDS.register(type.name) { -> BaseFlowingFluid.Source(fluidProperties) }
        sourceSupplier.supplier = sourceHolder

        val flowingHolder: Supplier<out FlowingFluid> = type.customFlowing
            ?: FLUIDS.register("${type.name}_flow") { -> BaseFlowingFluid.Flowing(fluidProperties) }
        flowingSupplier.supplier = flowingHolder

        val blockHolder = type.customBlock
            ?: BlockLoader.BLOCKS.register("${type.name}_block") { ->
            LiquidBlock(sourceHolder.get(), type.blockProperties)
        }
        blockSupplier.supplier = blockHolder

        val bucketHolder = type.customBucket
            ?: ItemLoader.register("${type.name}_bucket", bucketPriority) {
            BucketItem(sourceHolder.get(), Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1))
        }
        bucketSupplier.supplier = bucketHolder

        fluidsToRender.add(FluidToRender(sourceHolder, renderType))
        fluidsToRender.add(FluidToRender(flowingHolder, renderType))

        return LoadedFluid(typeHolder, sourceHolder, flowingHolder, blockHolder, bucketHolder)
    }

    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        event.enqueueWork {
            for (fluid in fluidsToRender) {
                ItemBlockRenderTypes.setRenderLayer(fluid.fluidHolder.get(), fluid.renderType)
            }
        }
    }

    val DIGESTIVE_JUICE = register(DigestiveJuiceFluid())
}

private operator fun <T> Supplier<T>.getValue(loadedFluid: FluidLoader.LoadedFluid, property: KProperty<*>): T {
    return this.get()
}
