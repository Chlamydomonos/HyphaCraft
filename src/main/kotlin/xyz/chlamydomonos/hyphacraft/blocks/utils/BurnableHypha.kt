package xyz.chlamydomonos.hyphacraft.blocks.utils

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

interface BurnableHypha {
    fun onBurnt(state: BlockState, level: Level, pos: BlockPos, replacing: Boolean)
}