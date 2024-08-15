package xyz.chlamydomonos.hyphacraft.blocks.base

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.loaders.DamageTypeLoader

open class ImmuneToHyphaExplosionBlock(properties: Properties) : BaseHyphaBlock(properties) {
    override fun onBlockExploded(state: BlockState, level: Level, pos: BlockPos, explosion: Explosion) {
        if (level.isClientSide) {
            return
        }

        if (DamageTypeLoader.HYPHA_EXPLOSION(level).key?.let { explosion.damageSource.`is`(it) } != true) {
            super.onBlockExploded(state, level, pos, explosion)
        }
    }
}