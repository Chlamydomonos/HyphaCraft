package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.loaders.DamageTypeLoader

class LooseFungusRootBlock : Block(
    Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.FUNGUS).noOcclusion()
), BurnableHypha {
    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 20

    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 100

    override fun onBlockExploded(state: BlockState, level: Level, pos: BlockPos, explosion: Explosion) {
        if (level.isClientSide) {
            return
        }
        val damageType = explosion.damageSource.type()
        if (damageType != DamageTypeLoader.HYPHA_EXPLOSION(level).value()) {
            super.onBlockExploded(state, level, pos, explosion)
        }
    }
}