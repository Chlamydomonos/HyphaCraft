package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.material.Fluid
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.fluids.BaseFluidType
import xyz.chlamydomonos.hyphacraft.fluids.DigestiveJuiceFluid
import java.util.function.Supplier

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
object FluidLoader {
    class DeferredSupplier<T> : Supplier<T> {
        lateinit var supplier: Supplier<out T>
        override fun get() = supplier.get()
    }

    class LoadedFluid (
        typeHolder: DeferredHolder<FluidType, FluidType>,
        sourceHolder: DeferredHolder<Fluid, BaseFlowingFluid.Source>,
        flowingHolder: DeferredHolder<Fluid, BaseFlowingFluid.Flowing>,
        blockHolder: DeferredHolder<Block, LiquidBlock>,
        bucketHolder: DeferredHolder<Item, BucketItem>
    ) {
        val type by typeHolder
        val source by sourceHolder
        val flowing by flowingHolder
        val block by blockHolder
        val bucket by bucketHolder
    }

    data class FluidToRender(
        val fluidHolder: DeferredHolder<Fluid, out Fluid>,
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

        val sourceHolder = FLUIDS.register(type.name) { -> BaseFlowingFluid.Source(fluidProperties) }
        sourceSupplier.supplier = sourceHolder

        val flowingHolder = FLUIDS.register("${type.name}_flow") { -> BaseFlowingFluid.Flowing(fluidProperties) }
        flowingSupplier.supplier = flowingHolder

        val blockHolder = BlockLoader.BLOCKS.register("${type.name}_block") { ->
            LiquidBlock(sourceHolder.get(), type.blockProperties)
        }
        blockSupplier.supplier = blockHolder

        val bucketHolder = ItemLoader.register("${type.name}_bucket", bucketPriority) {
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