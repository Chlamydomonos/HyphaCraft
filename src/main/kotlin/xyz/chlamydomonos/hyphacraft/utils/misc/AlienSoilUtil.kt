package xyz.chlamydomonos.hyphacraft.utils.misc

import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.config.ModConfigEvent
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.loaders.ConfigLoader
import java.util.stream.Collectors

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
object AlienSoilUtil {
    private lateinit var ALIEN_SOIL_DESTROYED: Set<Block>

    @SubscribeEvent
    fun onConfig(event: ModConfigEvent) {
        ALIEN_SOIL_DESTROYED = ConfigLoader.ALIEN_SOIL_DESTROYED.get().stream().map {
            BuiltInRegistries.BLOCK.get(ResourceLocation.parse(it))
        }.collect(Collectors.toSet())
    }

    fun onAlienSoilPlaced(level: ServerLevel, pos: BlockPos) {
        if (level.getBlockState(pos.above()).block in ALIEN_SOIL_DESTROYED) {
            level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 3)
        }
    }
}