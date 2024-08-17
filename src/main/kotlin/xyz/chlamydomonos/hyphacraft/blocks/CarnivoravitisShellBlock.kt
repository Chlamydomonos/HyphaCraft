package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.MapColor
import xyz.chlamydomonos.hyphacraft.blocks.base.ImmuneToHyphaExplosionBlock
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class CarnivoravitisShellBlock : ImmuneToHyphaExplosionBlock(
    Properties.ofFullCopy(Blocks.OAK_PLANKS)
        .sound(SoundType.FUNGUS)
        .randomTicks()
        .mapColor(MapColor.TERRACOTTA_CYAN)
        .ignitedByLava()
) {
    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.CAN_SECRETE, false))
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (!state.getValue(ModProperties.CAN_SECRETE)) {
            return
        }

        var firstEmptyPos = pos
        var lastShellPos = pos
        for (i in 1..6) {
            val newPos = pos.offset(0, i, 0)
            val newState = level.getBlockState(newPos)
            val fluid = level.getFluidState(newPos)
            if (newState.`is`(this) || newState.`is`(BlockLoader.CARNIVORAVITIS_ROOT)) {
                lastShellPos = newPos
                break
            }
            if (newState.isEmpty && fluid.isEmpty && firstEmptyPos == pos) {
                firstEmptyPos = newPos
            }
        }

        if (firstEmptyPos != pos && firstEmptyPos.y < lastShellPos.y) {
            level.setBlock(firstEmptyPos, BlockLoader.DIGESTIVE_JUICE_BLOCK.defaultBlockState(), 3)
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.CAN_SECRETE)
    }

    override fun onBurnt(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        replacing: Boolean,
        random: RandomSource
    ): BurnableHypha.VanillaBehaviourHandler {
        level.setBlock(pos, BlockLoader.HYPHACOAL_BLOCK.block.defaultBlockState(), 3)
        return BurnableHypha.VanillaBehaviourHandler.CANCEL
    }
}