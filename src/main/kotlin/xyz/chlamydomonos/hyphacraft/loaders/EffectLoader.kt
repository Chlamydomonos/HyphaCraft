package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.core.registries.Registries
import net.minecraft.world.effect.MobEffect
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.effects.CoveredWithSporeEffect

object EffectLoader {
    private val EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, HyphaCraft.MODID)

    fun register(bus: IEventBus) {
        EFFECTS.register(bus)
    }

    fun <T : MobEffect> register(name: String, effect: () -> T): DeferredHolder<MobEffect, T> {
        return EFFECTS.register(name, effect)
    }

    val COVERED_WITH_SPORE = register("covered_with_spore", ::CoveredWithSporeEffect)
}