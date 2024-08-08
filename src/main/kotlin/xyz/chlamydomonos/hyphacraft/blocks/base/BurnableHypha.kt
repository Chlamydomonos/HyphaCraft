package xyz.chlamydomonos.hyphacraft.blocks.base

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

interface BurnableHypha {
    enum class VanillaBehaviourHandler { DO, CANCEL }
    fun onBurnt(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        replacing: Boolean,
        random: RandomSource
    ): VanillaBehaviourHandler {
        return VanillaBehaviourHandler.DO
    }
}