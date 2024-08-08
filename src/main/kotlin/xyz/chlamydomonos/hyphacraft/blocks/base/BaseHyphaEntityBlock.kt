package xyz.chlamydomonos.hyphacraft.blocks.base

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha

abstract class BaseHyphaEntityBlock(properties: Properties) : BaseEntityBlock(properties), BurnableHypha {
    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5

    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getRenderShape(state: BlockState) = RenderShape.MODEL
}