package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.data.DataProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.datagen.*

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
object DataGenLoader {
    @SubscribeEvent
    fun onGatherData(event: GatherDataEvent) {
        val efh = event.existingFileHelper
        val lp = event.lookupProvider

        event.generator.addProvider(
            event.includeClient(),
            DataProvider.Factory { ModBlockStateProvider(it, efh) }
        )
        event.generator.addProvider(
            event.includeServer(),
            DataProvider.Factory { ModLootTableProvider(it, lp) }
        )
        event.generator.addProvider(
            event.includeServer(),
            DataProvider.Factory { ModBlockTagsProvider(it, lp, efh) }
        )
        event.generator.addProvider(
            event.includeClient(),
            DataProvider.Factory { ModItemModelProvider(it, efh) }
        )
        event.generator.addProvider(
            event.includeServer(),
            DataProvider.Factory { ModRecipeProvider(it, lp) }
        )
        event.generator.addProvider(
            event.includeServer(),
            DataProvider.Factory { ModDatapackBuiltinEntriesProvider(it, lp) }
        )
        event.generator.addProvider(
            event.includeServer(),
            DataProvider.Factory { ModEntityTagsProvider(it, lp, efh) }
        )
    }
}

