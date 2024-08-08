package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaBlock
import xyz.chlamydomonos.hyphacraft.loaders.DamageTypeLoader

class ActiveHyphaBlock : BaseHyphaBlock(
    Properties.ofFullCopy(Blocks.DIRT).randomTicks().sound(SoundType.SLIME_BLOCK)
) {
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