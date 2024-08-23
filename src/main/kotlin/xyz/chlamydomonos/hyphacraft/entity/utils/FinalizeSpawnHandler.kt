package xyz.chlamydomonos.hyphacraft.entity.utils

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent
import xyz.chlamydomonos.hyphacraft.entity.entities.HumifossorEntity

@EventBusSubscriber
object FinalizeSpawnHandler {
    @SubscribeEvent
    fun onFinalizeSpawn(event: FinalizeSpawnEvent) {
        val entity = event.entity
        if (entity is HumifossorEntity) {
            entity.charged = false
        }
    }
}