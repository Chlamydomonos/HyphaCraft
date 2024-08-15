package xyz.chlamydomonos.hyphacraft.utils.plant

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.config.ModConfigEvent
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.ConfigLoader
import xyz.chlamydomonos.hyphacraft.utils.CommonUtil

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
object FulgurfungusUtil {
    const val GROWTH_RATE = 1.0f / 20.0f

    var AFFECT_CREATIVE_PLAYER = false

    @SubscribeEvent
    fun onConfig(event: ModConfigEvent) {
        AFFECT_CREATIVE_PLAYER = ConfigLoader.FULGURFUNGUS_AFFECT_CREATIVE_PLAYER.get()
    }

    fun tryGrow(level: ServerLevel, pos: BlockPos) {
        if (CommonUtil.isNearFire(level, pos)) {
            return
        }

        for (i in -10..10) {
            for (j in -6..6) {
                for (k in -10..10) {
                    if (level.getBlockState(pos.offset(i, j, k)).`is`(BlockLoader.FULGURFUNGUS.block)) {
                        return
                    }
                }
            }
        }

        level.setBlock(pos, BlockLoader.FULGURFUNGUS.block.defaultBlockState(), 3)
    }
}