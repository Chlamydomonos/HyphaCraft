package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.core.registries.Registries
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.loot.BlockCopierLoot

object LootLoader {
    val LOOT_POOL_ENTRY_TYPES = DeferredRegister.create(Registries.LOOT_POOL_ENTRY_TYPE, HyphaCraft.MODID)

    fun register(bus: IEventBus) {
        LOOT_POOL_ENTRY_TYPES.register(bus)
    }

    val BLOCK_COPIER by LOOT_POOL_ENTRY_TYPES.register("block_copier") { -> LootPoolEntryType(BlockCopierLoot.CODEC) }
}