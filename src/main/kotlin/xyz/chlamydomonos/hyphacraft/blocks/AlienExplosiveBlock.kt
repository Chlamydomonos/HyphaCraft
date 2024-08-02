package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha

class AlienExplosiveBlock : Block(
    Properties
        .ofFullCopy(Blocks.BEDROCK)
        .randomTicks()
        .noOcclusion()
        .explosionResistance(0.000000001f)
) {
    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, state.block, 5)
        }
    }

    override fun onBlockExploded(state: BlockState, level: Level, pos: BlockPos, explosion: Explosion) {
        super.onBlockExploded(state, level, pos, explosion)
        if (level.isClientSide) {
            val random = level.random
            val particleCount = random.nextIntBetweenInclusive(100, 1000)
            for (i in 1..particleCount) {
                val randomX = random.nextGaussian() * 16
                val randomY = random.nextGaussian() * 16
                val randomZ = random.nextGaussian() * 16
                level.addParticle(
                    ParticleTypes.FLAME,
                    pos.x + randomX,
                    pos.y + randomY,
                    pos.z + randomZ,
                    0.0,
                    0.0,
                    0.0
                )
            }
        }
    }

    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if(!state.`is`(this) || !level.isAreaLoaded(pos, 10)) {
            return
        }
        for (i in -7..7) {
            for (j in -7..7) {
                for (k in -7..7) {
                    val stateToTransfer = level.getBlockState(pos.offset(i, j, k))
                    if (stateToTransfer.block is BurnableHypha) {
                        level.setBlock(pos.offset(i, j, k), defaultBlockState(), 3)
                    }
                }
            }
        }
        level.explode(
            null,
            pos.x.toDouble(),
            pos.y.toDouble(),
            pos.z.toDouble(),
            4f,
            Level.ExplosionInteraction.BLOCK)
    }
}