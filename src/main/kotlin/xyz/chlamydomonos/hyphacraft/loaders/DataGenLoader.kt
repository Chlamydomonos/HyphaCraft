package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.data.DataProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockTagsProvider
import xyz.chlamydomonos.hyphacraft.datagen.ModLootTableProvider

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
object DataGenLoader {
    @SubscribeEvent
    fun onGatherData(event: GatherDataEvent) {
        val efh = event.existingFileHelper
        val lp = event.lookupProvider

        event.generator.addProvider(
            event.includeClient(),
            DataProvider.Factory { output -> ModBlockStateProvider(output, efh) }
        )
        event.generator.addProvider(
            event.includeServer(),
            DataProvider.Factory { output -> ModLootTableProvider(output, lp) }
        )
        event.generator.addProvider(
            event.includeServer(),
            DataProvider.Factory { output -> ModBlockTagsProvider(output, lp, efh) }
        )
    }
}

