package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CarpetBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha
import xyz.chlamydomonos.hyphacraft.entity.ModEntityTags
import xyz.chlamydomonos.hyphacraft.loaders.BiomeLoader
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.DataAttachmentLoader
import xyz.chlamydomonos.hyphacraft.loaders.EffectLoader

class SporeHeapBlock : CarpetBlock(
    Properties.ofFullCopy(Blocks.WHITE_CARPET)
        .sound(SoundType.SNOW)
        .randomTicks()
        .noCollission()
        .ignitedByLava()
), BurnableHypha {
    companion object {
        const val BIOME_CHANGE_THRESHOLD = 80
    }

    override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
        if(level.isClientSide) {
            return
        }

        val effect1 = MobEffectInstance(MobEffects.POISON, 100, 0)
        val effect2 = MobEffectInstance(EffectLoader.COVERED_WITH_SPORE, 50, 0)
        if(entity is LivingEntity) {
            if (!entity.type.`is`(ModEntityTags.HYPHACRAFT_INSECT)) {
                entity.addEffect(effect1)
            }
            entity.addEffect(effect2)
        }
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        val belowState = level.getBlockState(pos.below())
        return isFaceFull(belowState.getCollisionShape(level, pos.below()), Direction.UP)
    }

    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5

    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction) = 5
    override fun onBurnt(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        replacing: Boolean,
        random: RandomSource
    ): BurnableHypha.VanillaBehaviourHandler {
        if(random.nextInt(3) == 0) {
            val explosionRadius = random.nextFloat() * 4 + 5
            level.explode(
                null,
                pos.x.toDouble(),
                pos.y.toDouble(),
                pos.z.toDouble(),
                explosionRadius,
                false,
                Level.ExplosionInteraction.BLOCK
            )
            return BurnableHypha.VanillaBehaviourHandler.CANCEL
        }
        return BurnableHypha.VanillaBehaviourHandler.DO
    }

    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
        if (level.isClientSide) {
            return
        }

        val chunk = level.getChunkAt(pos)
        val heapCount = chunk.getData(DataAttachmentLoader.SPORE_HEAP_COUNT)
        chunk.setData(DataAttachmentLoader.SPORE_HEAP_COUNT, heapCount + 1)
        val isAlienForest = chunk.getData(DataAttachmentLoader.IS_ALIEN_FOREST)
        if (heapCount >= BIOME_CHANGE_THRESHOLD - 1 && !isAlienForest) {
            level.scheduleTick(pos, this, 1)
        }
    }

    override fun destroy(level: LevelAccessor, pos: BlockPos, state: BlockState) {
        if (level.isClientSide) {
            return
        }

        val chunk = level.getChunk(pos)
        val heapCount = chunk.getData(DataAttachmentLoader.SPORE_HEAP_COUNT)
        chunk.setData(DataAttachmentLoader.SPORE_HEAP_COUNT, heapCount - 1)
    }

    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val chunk = level.getChunkAt(pos)
        chunk.fillBiomesFromNoise(
            { _, _, _, _ -> BiomeLoader.ALIEN_FOREST.get(level) },
            level.chunkSource.randomState().sampler()
        )
        chunk.setData(DataAttachmentLoader.IS_ALIEN_FOREST, true)
        level.chunkSource.chunkMap.resendBiomesForChunks(listOf(chunk))
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (!level.getChunkAt(pos).getData(DataAttachmentLoader.IS_ALIEN_FOREST)) {
            return
        }

        val newPos = pos.below()
        val newState = level.getBlockState(newPos)
        if (newState.`is`(BlockLoader.ALIEN_SOIL.block)) {
            level.setBlock(newPos, BlockLoader.ALIEN_SWARD.block.defaultBlockState(), 3)
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3)
        } else if (newState.`is`(BlockLoader.ALIEN_SWARD.block)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3)
            if (random.nextInt(5) == 0) {
                level.setBlock(newPos, BlockLoader.FERTILE_ALIEN_SWARD.block.defaultBlockState(), 3)
            }
        } else if (newState.`is`(BlockLoader.TUMIDUSIO.block)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3)
            level.setBlock(newPos, BlockLoader.HARDENED_FUNGUS_SHELL.block.defaultBlockState(), 3)
        }
    }
}