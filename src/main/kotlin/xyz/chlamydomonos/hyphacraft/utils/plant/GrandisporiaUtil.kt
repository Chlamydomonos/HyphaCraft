package xyz.chlamydomonos.hyphacraft.utils.plant

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader


object GrandisporiaUtil {
    const val INITIAL_GROW_RATE = 1.0f / 30.0f

    fun tryGrowInitialStipe(level: ServerLevel, pos: BlockPos) {
        if (!level.getBlockState(pos).isEmpty) {
            return
        }
        for (i in -5..5) {
            for (j in -5..5) {
                for (k in -5..5) {
                    val state = level.getBlockState(pos.offset(i, j, k))
                    if (state.`is`(BlockLoader.GRANDISPORIA_STIPE) || state.`is`(BlockLoader.GRANDISPORIA_WITHERED_STIPE.block)) {
                        return
                    }
                }
            }
        }
        level.setBlock(pos, BlockLoader.GRANDISPORIA_STIPE.defaultBlockState(), 3)
    }

    private fun canGrowTo(
        xOffset: Int,
        zOffset: Int,
        isAirBuffer: Array<Array<BooleanArray>>,
        isThisBuffer: Array<Array<BooleanArray>>
    ): Boolean {
        if (xOffset == 0 && zOffset == 0) {
            for (i in 1..3) {
                for (j in 3..4) {
                    for (k in 1..3) {
                        if (isThisBuffer[i][j][k]) return false
                    }
                }
            }
            return isAirBuffer[2][3][2] && isAirBuffer[2][4][2]
        }

        return (isAirBuffer[xOffset + 2][2][zOffset + 2]
                && isAirBuffer[xOffset + 2][3][zOffset + 2]
                && !isThisBuffer[xOffset * 2 + 2][2][zOffset * 2 + 2]
                && !isThisBuffer[xOffset * 2 + 2][3][zOffset * 2 + 2]
                && !isThisBuffer[xOffset + 2][1][zOffset + 2])
    }

    private fun tryIncreaseHeight(state: BlockState, random: RandomSource): BlockState {
        return if (state.getValue(ModProperties.HEIGHT) == 0 || random.nextInt(10) == 0) {
            state.cycle(ModProperties.HEIGHT)
        } else {
            state
        }
    }

    private fun canGenerateCap(isAirBuffer: Array<Array<BooleanArray>>): Boolean {
        for (i in 1..3) {
            for(k in 1..3) {
                if (!isAirBuffer[i][3][k]) {
                    return false
                }
            }
        }
        return true
    }

    private fun growUp(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        random: RandomSource,
        isAirBuffer: Array<Array<BooleanArray>>,
        isThisBuffer: Array<Array<BooleanArray>>
    ): Boolean {

        val availableDirections = mutableSetOf<Vec3i>()

        if (canGrowTo(-1, 0, isAirBuffer, isThisBuffer)) {
            availableDirections.add(Vec3i(-1, 0, 0))
        }
        if (canGrowTo(1, 0, isAirBuffer, isThisBuffer)) {
            availableDirections.add(Vec3i(1, 0, 0))
        }
        if (canGrowTo(0, -1, isAirBuffer, isThisBuffer)) {
            availableDirections.add(Vec3i(0, 0, -1))
        }
        if(canGrowTo(0, 1, isAirBuffer, isThisBuffer)) {
            availableDirections.add(Vec3i(0, 0, 1))
        }

        var canSplitX = Vec3i(-1, 0, 0) in availableDirections && Vec3i(1, 0, 0) in availableDirections
        var canSplitZ = Vec3i(0, 0, -1) in availableDirections && Vec3i(0, 0, 1) in availableDirections
        val canGrowUp = canGrowTo(0, 0, isAirBuffer, isThisBuffer)

        if (!level.getBlockState(pos.below()).`is`(state.block)) {
            availableDirections.clear()
            canSplitX = false
            canSplitZ = false
        }

        val canGrowSide = availableDirections.isNotEmpty()

        val newState = state.block.defaultBlockState()
            .setValue(ModProperties.HEIGHT, state.getValue(ModProperties.HEIGHT))

        if (canSplitX || canSplitZ) {
            val temp = random.nextInt(4) == 0
            if (temp) {
                if (canSplitX && canSplitZ) {
                    val temp2 = random.nextBoolean()
                    if (temp2) canSplitX = false
                }

                if (canSplitX) {
                    level.setBlock(pos.offset(-1, 0, 0), tryIncreaseHeight(newState, random), 3)
                    level.setBlock(pos.offset(1, 0, 0), tryIncreaseHeight(newState, random), 3)
                    return true
                }

                level.setBlock(pos.offset(0, 0, -1), tryIncreaseHeight(newState, random), 3)
                level.setBlock(pos.offset(0, 0, 1), tryIncreaseHeight(newState, random), 3)
                return true
            }
        }

        if (random.nextInt(10) == 0) return false

        if (canGrowUp && (random.nextBoolean() || !canGrowSide)) {
            level.setBlock(pos.above(), tryIncreaseHeight(newState, random), 3)
            return true
        }

        if (canGrowSide) {
            val dir: Any = availableDirections.toTypedArray()[random.nextInt(availableDirections.size)]
            level.setBlock(pos.offset(dir as Vec3i), tryIncreaseHeight(newState, random), 3)
        }

        return false
    }

    private fun isOlder(state: BlockState, age: Int): Boolean {
        return state.`is`(BlockLoader.GRANDISPORIA_WITHERED_STIPE.block) ||
                (state.`is`(BlockLoader.GRANDISPORIA_STIPE ) && state.getValue(ModProperties.AGE) > age)
    }

    private fun canGrowOld(state: BlockState, level: ServerLevel, pos: BlockPos): Boolean {
        val age = state.getValue(ModProperties.AGE)
        val stateBelow = level.getBlockState(pos.below())
        val stateNorth = level.getBlockState(pos.north())
        val stateEast = level.getBlockState(pos.east())
        val stateSouth = level.getBlockState(pos.south())
        val stateWest = level.getBlockState(pos.west())

        return isOlder(stateBelow, age) ||
                isOlder(stateNorth, age) ||
                isOlder(stateEast, age) ||
                isOlder(stateSouth, age) ||
                isOlder(stateWest, age)
    }

    fun grow(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val isAirBuffer = Array(5) { Array(5) { BooleanArray(5) } }
        val isThisBuffer = Array(5) { Array(5) { BooleanArray(5) } }

        for (i in -2..2) {
            for (j in -2..2) {
                for (k in -2..2) {
                    val tempState = level.getBlockState(pos.offset(i, j, k))
                    isAirBuffer[i + 2][j + 2][k + 2] = tempState.isEmpty
                    isThisBuffer[i + 2][j + 2][k + 2] = tempState.`is`(state.block)
                }
            }
        }

        val height = state.getValue(ModProperties.HEIGHT)
        val age = state.getValue(ModProperties.AGE)
        val maxBuildHeight = level.maxBuildHeight - 4

        if(age == 0 && height < 4 && pos.y < maxBuildHeight) {
            val growSuccess = growUp(level, pos, state, random, isAirBuffer, isThisBuffer)
            val newState = state.cycle(ModProperties.AGE)
            level.setBlock(pos, newState, 3)

            if(!growSuccess && isAirBuffer[2][3][2]) {
                level.setBlock(
                    pos.above(),
                    BlockLoader.GRANDISPORIA_SMALL_CAP.defaultBlockState()
                        .setValue(ModProperties.CAN_GROW, canGenerateCap(isAirBuffer)),
                    3
                )
            }
            return
        }

        if (height == 0 && age > 0 && age < 4 && random.nextInt(5) == 0) {
            if (age == 3) {
                level.setBlock(pos, BlockLoader.GRANDISPORIA_WITHERED_STIPE.block.defaultBlockState(), 3)
            } else {
                level.setBlock(pos, state.cycle(ModProperties.AGE), 3)
            }
            return
        }

        if (canGrowOld(state, level, pos) && random.nextInt(8) == 0) {
            if (age == 3) {
                level.setBlock(pos, BlockLoader.GRANDISPORIA_WITHERED_STIPE.block.defaultBlockState(), 3)
            } else {
                level.setBlock(pos, state.cycle(ModProperties.AGE), 3)
            }
        }
    }
}