package xyz.chlamydomonos.hyphacraft.utils.plant

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.PipeBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.config.ModConfigEvent
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.minus
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.blockentities.CarnivoravitisVineBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.BlockTagLoader
import xyz.chlamydomonos.hyphacraft.loaders.ConfigLoader
import xyz.chlamydomonos.hyphacraft.utils.CommonUtil

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
object CarnivoravitisUtil {
    const val EXPAND_RATE = 1.0f / 5.0f

    var AFFECT_CREATIVE_PLAYER = false

    @SubscribeEvent
    fun onConfig(event: ModConfigEvent) {
        AFFECT_CREATIVE_PLAYER = ConfigLoader.CARNIVORAVITIS_AFFECT_CREATIVE_PLAYER.get()
    }

    fun canGrowRoot(level: ServerLevel, pos: BlockPos): Boolean {
        if (pos.y < level.minBuildHeight + 20) {
            return false
        }

        if (CommonUtil.isNearFire(level, pos, Vec3i(0, -5, 0), Vec3i(11, 9, 11))) {
            return false
        }

        var emptyCount = 0
        for (i in -3..3) {
            for (j in -7..-1) {
                for (k in -3..3) {
                    val newState = level.getBlockState(pos.offset(i, j, k))
                    if (newState.`is`(BlockTagLoader.CARNIVORAVITIS_PLANT)) {
                        return false
                    }
                    if (newState.isEmpty) {
                        emptyCount++
                    }
                }
            }
        }

        return emptyCount <= 170
    }

    fun tryGrowInitial(level: ServerLevel, pos: BlockPos) {
        if (!level.getBlockState(pos).`is`(BlockLoader.ACTIVE_HYPHA_BLOCK.block)) {
            return
        }

        for (i in 1..5) {
            if (!level.getBlockState(pos.offset(0, i, 0)).isEmpty) {
                return
            }
        }

        for (i in -4..4) {
            for (j in 1..2) {
                for (k in -4..4) {
                    if (level.getBlockState(pos.offset(i, j, k)).`is`(BlockTagLoader.CARNIVORAVITIS_PLANT)) {
                        return
                    }
                }
            }
        }

        if (!canGrowRoot(level, pos)) {
            return
        }

        level.setBlock(
            pos,
            BlockLoader.CARNIVORAVITIS_ROOT.defaultBlockState().setValue(ModProperties.AGE, 0),
            3
        )
        level.setBlock(pos.above(), BlockLoader.CARNIVORAVITIS_SEEDLING.defaultBlockState(), 3)
    }

    fun growRoot(level: ServerLevel, pos: BlockPos) {
        val shell = BlockLoader.CARNIVORAVITIS_SHELL.block.defaultBlockState()
        val fluid = BlockLoader.DIGESTIVE_JUICE_BLOCK.defaultBlockState()
        for (i in -2..2) {
            for (k in -2..2) {
                if (i != 0 || k != 0) {
                    level.setBlock(pos.offset(i, -1, k), shell, 3)
                } else {
                    level.setBlock(pos.offset(i, -1, k), BlockLoader.CARNIVORAVITIS_ROOT.defaultBlockState(), 3)
                }
                level.setBlock(pos.offset(i, -7, k), shell.setValue(ModProperties.CAN_SECRETE, true), 3)
            }
        }
        for (i in -3..3) {
            for (j in -6..-2) {
                for (k in -3..3) {
                    if ((i == -3 || i == 3) && (k == -3 || k == 3)) {
                        continue
                    }
                    if (i == -3 || i == 3 || k == -3 || k == 3) {
                        level.setBlock(pos.offset(i, j, k), shell, 3)
                        continue
                    }
                    if (j == -6) {
                        level.setBlock(pos.offset(i, j, k), fluid,3 )
                        continue
                    }
                    level.setBlock(pos.offset(i, j, k), Blocks.AIR.defaultBlockState(), 3)
                }
            }
        }
    }

    fun growVine(level: ServerLevel, pos: BlockPos, state: BlockState, random: RandomSource) {
        if (random.nextBoolean()) {
            return
        }

        val nextPos = (level.getBlockEntity(pos) as CarnivoravitisVineBlockEntity).nextPos
        val delta = pos - nextPos
        val direction = Direction.fromDelta(delta.x, delta.y, delta.z) ?: return

        if (delta.y == 0) {
            growVineHorizontal(level, pos, state, direction, random)
            return
        }
        growVineVertical(level, pos, state, random)
    }

    private fun growVineHorizontal(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        direction: Direction,
        random: RandomSource
    ) {
        val age = state.getValue(ModProperties.AGE)
        if (age >= 2) {
            return
        }
        if (age == 1) {
            tryGrowFlower(level, pos, state, random)
            return
        }

        level.setBlock(pos, state.cycle(ModProperties.AGE), 3)

        val height = state.getValue(ModProperties.HEIGHT)
        if (height == 3) {
            return
        }

        val availableDirections = arrayListOf<Direction>()
        val otherDirections = listOf(direction.clockWise, direction.counterClockWise, Direction.UP, Direction.DOWN)
        if (level.getBlockState(pos.offset(direction.normal)).isEmpty) {
            for (i in 1..10) {
                availableDirections.add(direction)
            }
        }
        for (dir in otherDirections) {
            val newPos = pos.offset(dir.normal)
            val newPos2 = newPos.offset(direction.normal)
            if (level.getBlockState(newPos).isEmpty && level.getBlockState(newPos2).isEmpty) {
                availableDirections.add(dir)
            }
        }

        if (availableDirections.size == 0) {
            return
        }

        val randomDirection = availableDirections[random.nextInt(availableDirections.size)]
        val state1 = BlockLoader.CARNIVORAVITIS_VINE.defaultBlockState()
            .setValue(ModProperties.HEIGHT, 1)
            .setValue(ModProperties.AGE, 0)
        val state3 = state1.setValue(ModProperties.HEIGHT, 3)
        val randomState = if (random.nextInt(10) == 0) state3 else state1
        if (randomDirection == direction) {
            growVineTo(level, pos, direction, randomState)
        } else {
            val newPos = pos.offset(randomDirection.normal)
            growVineTo(level, pos, randomDirection, state3)
            growVineTo(level, newPos, direction, randomState)
        }
    }

    private fun growVineVertical(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        random: RandomSource
    ) {
        if (level.maxBuildHeight - pos.y <= 5) {
            return
        }

        val height = state.getValue(ModProperties.HEIGHT)
        val age = state.getValue(ModProperties.AGE)

        if (height == 3 || age >= 2) {
            return
        }

        if (height == 0) {
            if (age == 0 && level.getBlockState(pos.above()).isEmpty) {
                val state1 = BlockLoader.CARNIVORAVITIS_VINE.defaultBlockState().setValue(ModProperties.HEIGHT, 1)
                val state3 = state1.setValue(ModProperties.HEIGHT, 3)
                var newPos = pos
                for (i in 1..3) {
                    growVineTo(level, newPos, Direction.UP, state3)
                    newPos = newPos.above()
                }
                growVineTo(level, newPos, Direction.UP, state1)
                level.setBlock(pos, state.cycle(ModProperties.AGE), 3)
            }
            return
        }

        if (height == 2) {
            if (age == 0) {
                level.setBlock(pos, state.cycle(ModProperties.AGE), 3)
                val availableDirections = arrayListOf<Direction>()
                val possibleDirections = listOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)
                for (dir in possibleDirections) {
                    if (level.getBlockState(pos.offset(dir.normal)).isEmpty) {
                        availableDirections.add(dir)
                    }
                }
                if (availableDirections.isEmpty()) {
                    return
                }
                val randomDirection = availableDirections[random.nextInt(availableDirections.size)]
                val newState = BlockLoader.CARNIVORAVITIS_VINE.defaultBlockState()
                    .setValue(ModProperties.HEIGHT, 1)
                growVineTo(level, pos, randomDirection, newState)
                return
            }

            if (!level.getBlockState(pos.above()).isEmpty || random.nextBoolean()) {
                return
            }

            val newState = BlockLoader.CARNIVORAVITIS_VINE.defaultBlockState().setValue(ModProperties.HEIGHT, 1)
            growVineTo(level, pos, Direction.UP, newState)
            level.setBlock(pos, state.cycle(ModProperties.AGE), 3)
            return
        }

        if (age >= 1) {
            return
        }

        val availableDirections = arrayListOf<Direction>()
        val possibleDirections = listOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)
        if (level.getBlockState(pos.above()).isEmpty) {
            for (i in 1..10) {
                availableDirections.add(Direction.UP)
            }
        }
        for (dir in possibleDirections) {
            val newPos = pos.offset(dir.normal)
            val newPos2 = newPos.above()
            if (level.getBlockState(newPos).isEmpty && level.getBlockState(newPos2).isEmpty) {
                availableDirections.add(dir)
            }
        }
        if (availableDirections.isEmpty()) {
            return
        }

        level.setBlock(pos, state.cycle(ModProperties.AGE), 3)
        val state1 = BlockLoader.CARNIVORAVITIS_VINE.defaultBlockState().setValue(ModProperties.HEIGHT, 1)
        val state2 = state1.setValue(ModProperties.HEIGHT, 2)
        val state3 = state2.setValue(ModProperties.HEIGHT, 3)
        val randomDirection = availableDirections[random.nextInt(availableDirections.size)]
        val randomState = if (random.nextInt(80) == 0) state3 else if (random.nextInt(5) == 0) state2 else state1
        if (randomDirection == Direction.UP) {
            growVineTo(level, pos, Direction.UP, randomState)
        } else {
            val newPos = pos.offset(randomDirection.normal)
            growVineTo(level, pos, randomDirection, state3)
            growVineTo(level, newPos, Direction.UP, randomState)
        }
    }

    private fun tryGrowFlower(level: ServerLevel, pos: BlockPos, state: BlockState, random: RandomSource) {
        for (i in -5..5) {
            for (j in -5..5) {
                for (k in -5..5) {
                    if (level.getBlockState(pos.offset(i, j, k)).`is`(BlockLoader.CARNIVORAVITIS_FLOWER)) {
                        return
                    }
                }
            }
        }

        val availableDirections = arrayListOf<Direction>()
        for (dir in Direction.entries) {
            if (level.getBlockState(pos.offset(dir.normal)).`is`(BlockTags.REPLACEABLE)) {
                availableDirections.add(dir)
            }
        }
        if (availableDirections.isEmpty()) {
            return
        }
        val direction = availableDirections[random.nextInt(availableDirections.size)]
        level.setBlock(
            pos.offset(direction.normal),
            BlockLoader.CARNIVORAVITIS_FLOWER.defaultBlockState().setValue(ModProperties.DIRECTION, direction),
            3
        )
        level.setBlock(pos, state.setValue(ModProperties.AGE, 3), 3)
    }

    private fun growVineTo(
        level: ServerLevel,
        origin: BlockPos,
        direction: Direction,
        state: BlockState
    ) {
        val pos = origin.offset(direction.normal)
        val newState = state.setValue(PipeBlock.PROPERTY_BY_DIRECTION[direction.opposite]!!, true)
        level.setBlock(pos, newState, 3)
        val be = level.getBlockEntity(pos) as CarnivoravitisVineBlockEntity
        be.nextPos = origin
    }
}