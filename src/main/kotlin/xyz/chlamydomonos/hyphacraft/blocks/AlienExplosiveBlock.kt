package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.loaders.BiomeLoader
import xyz.chlamydomonos.hyphacraft.loaders.DamageTypeLoader
import xyz.chlamydomonos.hyphacraft.loaders.DataAttachmentLoader

class AlienExplosiveBlock : Block(
    Properties
        .ofFullCopy(Blocks.BEDROCK)
        .randomTicks()
        .noOcclusion()
        .noLootTable()
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

        val chunk = level.getChunkAt(pos)
        if (chunk.getData(DataAttachmentLoader.IS_ALIEN_FOREST)) {
            chunk.setData(DataAttachmentLoader.IS_ALIEN_FOREST, false)
            chunk.fillBiomesFromNoise(
                { _, _, _, _ -> BiomeLoader.ANCIENT_ALIEN_FOREST(level) },
                level.chunkSource.randomState().sampler()
            )
            level.chunkSource.chunkMap.resendBiomesForChunks(listOf(chunk))
        }

        for (i in -7..7) {
            for (j in -7..7) {
                for (k in -7..7) {
                    val newPos = pos.offset(i, j, k)
                    val stateToTransfer = level.getBlockState(newPos)
                    val blockToTransfer = stateToTransfer.block
                    if (blockToTransfer is BurnableHypha) {
                        if (random.nextInt(4) == 0) {
                           val result = blockToTransfer.onBurnt(stateToTransfer, level, newPos, true, random)
                           if (result == BurnableHypha.VanillaBehaviourHandler.DO) {
                               level.setBlock(pos.offset(i, j, k), defaultBlockState(), 3)
                           }
                        } else {
                            level.setBlock(pos.offset(i, j, k), defaultBlockState(), 3)
                        }
                    }
                }
            }
        }
        level.explode(
            null,
            DamageSource(DamageTypeLoader.ALIEN_EXPLOSION(level)),
            null,
            pos.toVec3(),
            4f,
            true,
            Level.ExplosionInteraction.BLOCK
        )
    }
}