package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.MapColor
import xyz.chlamydomonos.hyphacraft.HyphaCraft

class TestBlock : Block(Properties.ofFullCopy(Blocks.BEDROCK).noLootTable().mapColor(MapColor.SNOW)) {
    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        HyphaCraft.LOGGER.debug("Tick Test Block")
        for (direction in Direction.entries) {
            HyphaCraft.LOGGER.debug("{}: {}", direction.getName(), direction.toYRot())
        }
    }
}