package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext
import net.neoforged.neoforge.client.model.generators.ModelProvider
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.plus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.times
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class GrandisporiaWitheredCapBlock : Block(
    Properties.ofFullCopy(Blocks.DIRT)
        .sound(SoundType.FUNGUS)
        .randomTicks()
) {
    init {
        registerDefaultState(defaultBlockState().setValue(ModProperties.SPORE_AMOUNT, 0))
    }

    companion object {
        fun genModel(provider: ModBlockStateProvider) {
            provider.simpleBlockWithItem(BlockLoader.GRANDISPORIA_WITHERED_CAP.block, provider.models().cubeBottomTop(
                NameUtil.path(BlockLoader.GRANDISPORIA_WITHERED_CAP.block),
                NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/grandisporia_withered_cap"),
                NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/grandisporia_withered_cap_bottom"),
                NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/grandisporia_withered_cap")
            ))
        }

        val PARTICLE by lazy {
            BlockParticleOption(ParticleTypes.FALLING_DUST, BlockLoader.SPORE_HEAP.block.defaultBlockState())
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(ModProperties.SPORE_AMOUNT)
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (!level.getBlockState(pos.below()).isEmpty) {
            return
        }

        val sporeAmount = state.getValue(ModProperties.SPORE_AMOUNT)
        if(sporeAmount == 0) {
            return
        }

        val randomX = random.nextGaussian() / 2
        val randomZ = random.nextGaussian() / 2
        val randomDir = Vec3(randomX, -1.0, randomZ).normalize()
        val below = pos.below().toVec3()
        val clipContext = ClipContext(
            below,
            below + randomDir * 256.0,
            ClipContext.Block.COLLIDER,
            ClipContext.Fluid.NONE,
            CollisionContext.empty()
        )
        val target = level.clip(clipContext).blockPos
        val targetState = level.getBlockState(target)
        if (targetState.isEmpty || target.y >= pos.y || targetState.`is`(BlockLoader.SPORE_HEAP.block)) {
            return
        }
        if (targetState.`is`(BlockLoader.ROTTEN_FUNGUS_HEAP.block)) {
            level.setBlock(target, BlockLoader.HUMUS_HEAP.block.defaultBlockState(), 3)
            level.setBlock(pos, state.setValue(ModProperties.SPORE_AMOUNT, sporeAmount - 1), 3)
            return
        } else if(level.getBlockState(target.above()).`is`(BlockLoader.ROTTEN_FUNGUS_HEAP.block)) {
            level.setBlock(target.above(), BlockLoader.HUMUS_HEAP.block.defaultBlockState(), 3)
            level.setBlock(pos, state.setValue(ModProperties.SPORE_AMOUNT, sporeAmount - 1), 3)
            return
        } else if (
            isFaceFull(targetState.getCollisionShape(level, target), Direction.UP) &&
            level.getBlockState(target.above()).isEmpty
            ) {
            level.setBlock(target.above(), BlockLoader.SPORE_HEAP.block.defaultBlockState(), 3)
            level.setBlock(pos, state.setValue(ModProperties.SPORE_AMOUNT, sporeAmount - 1), 3)
            return
        }

        if(random.nextInt(4) == 0) {
            level.setBlock(pos, state.setValue(ModProperties.SPORE_AMOUNT, sporeAmount - 1), 3)
        }
    }

    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
        if (!level.getBlockState(pos.below()).isEmpty) {
            return
        }

        if(state.getValue(ModProperties.SPORE_AMOUNT) == 0) {
            return
        }

        val particleCount = random.nextInt(3, 10)
        for (i in 1..particleCount) {
            val randomX = pos.x + random.nextDouble()
            val randomY = pos.y - 0.05
            val randomZ = pos.z + random.nextDouble()
            level.addParticle(PARTICLE, randomX, randomY, randomZ, 0.0, 0.0, 0.0)
        }
    }
}