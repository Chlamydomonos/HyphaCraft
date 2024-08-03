package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.loaders.DamageTypeLoader

class HyphaResidueBlock(properties: Properties) : Block(properties) {
    override fun onBlockExploded(state: BlockState, level: Level, pos: BlockPos, explosion: Explosion) {
        if (level.isClientSide) {
            super.onBlockExploded(state, level, pos, explosion)
            return
        }

        val dt = explosion.damageSource.type()
        if (dt == DamageTypeLoader.ALIEN_EXPLOSION(level).value()) {
            val randomNum = level.random.nextInt(2)
            if(randomNum == 0) {
                return
            }
        }

        super.onBlockExploded(state, level, pos, explosion)
    }
}