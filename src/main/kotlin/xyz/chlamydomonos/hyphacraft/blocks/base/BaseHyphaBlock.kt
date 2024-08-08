package xyz.chlamydomonos.hyphacraft.blocks.base

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha

open class BaseHyphaBlock(properties: Properties) : Block(properties), BurnableHypha {
    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5

    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5
}