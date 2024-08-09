package xyz.chlamydomonos.hyphacraft.fluids

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.fluids.FluidType

class DigestiveJuiceFluid : BaseFluidType(
    "digestive_juice",
    FluidType.Properties.create(),
    BlockBehaviour.Properties.ofFullCopy(Blocks.WATER),
    2,
    2,
    ResourceLocation.withDefaultNamespace("block/water_still"),
    ResourceLocation.withDefaultNamespace("block/water_flow"),
    ResourceLocation.withDefaultNamespace("block/water_overlay"),
    0xa06614a0,
    0xa06614,
    1.0f,
    6.0f
)