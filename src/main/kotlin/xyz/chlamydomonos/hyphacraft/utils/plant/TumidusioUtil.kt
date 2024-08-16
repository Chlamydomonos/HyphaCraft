package xyz.chlamydomonos.hyphacraft.utils.plant

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.config.ModConfigEvent
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.dot
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.unaryMinus
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.blockentities.TumidusioHyphaBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.BlockTagLoader
import xyz.chlamydomonos.hyphacraft.loaders.ConfigLoader
import xyz.chlamydomonos.hyphacraft.utils.CommonUtil
import java.util.stream.Collectors
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.min
import kotlin.math.sqrt

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
object TumidusioUtil {
    const val EXPAND_RATE = 1.0f / 3.0f
    private val INITIAL_EXPAND_DIRECTIONS: List<Vec3i>
    private val EXPAND_DIRECTIONS: List<Vec3i>
    private val EXPAND_LENGTHS: List<Double>

    private fun genInitialExpandDirections(): List<Vec3i> {
        val out = arrayListOf(Vec3i(0, -1, 0))

        for(i in -1..1) {
            for(k in -1..1) {
                out.add(Vec3i(i, -1, k))
            }
        }

        for(i in -1..1) {
            for(j in -1..1) {
                for(k in -1..1) {
                    if(i != 0 || j != 0 || k != 0) {
                        out.add(Vec3i(i, j, k))
                    }
                }
            }
        }

        return out
    }

    private fun getExpandDirections(): List<Vec3i> {
        val out = arrayListOf<Vec3i>()
        for(i in -1..1) {
            for(j in -1..1) {
                for(k in -1..1) {
                    if(i != 0 || j != 0 || k != 0) {
                        out.add(Vec3i(i, j, k))
                    }
                }
            }
        }
        return out
    }

    private fun getExpandLengths(): List<Double> {
        val out = arrayListOf<Double>()
        for(i in -1..1) {
            for(j in -1..1) {
                for(k in -1..1) {
                    if(i != 0 || j != 0 || k != 0) {
                        out.add(sqrt((i * i + j * j + k * k).toDouble()))
                    }
                }
            }
        }
        return out
    }

    init {
        INITIAL_EXPAND_DIRECTIONS = genInitialExpandDirections()
        EXPAND_DIRECTIONS = getExpandDirections()
        EXPAND_LENGTHS = getExpandLengths()
    }

    private lateinit var TUMIDUSIO_BLACKLIST: Set<Block>

    @SubscribeEvent
    fun onConfig(event: ModConfigEvent) {
        TUMIDUSIO_BLACKLIST = ConfigLoader.TUMIDUSIO_BLACKLIST.get().stream().map {
            BuiltInRegistries.BLOCK.get(ResourceLocation.parse(it))
        }.collect(Collectors.toSet())
    }

    fun setHypha(level: ServerLevel, pos: BlockPos, phase: Int = 0) {
        val state = level.getBlockState(pos)
        val oldBe = level.getBlockEntity(pos)
        var oldCopiedState: BlockState? = null
        if(oldBe is TumidusioHyphaBlockEntity) {
            oldCopiedState = oldBe.copiedState
        }

        level.setBlock(
            pos,
            BlockLoader.TUMIDUSIO_HYPHA.defaultBlockState().setValue(ModProperties.PHASE, phase),
            3
        )
        val be = level.getBlockEntity(pos) as TumidusioHyphaBlockEntity
        be.copiedState = oldCopiedState ?: state
    }

    fun canHyphaGrow(level: LevelReader, pos: BlockPos): Boolean {
        if (level is ServerLevel && !level.isLoaded(pos)) {
            return false
        }

        val state = level.getBlockState(pos)
        if(state.block in TUMIDUSIO_BLACKLIST) {
            return false
        }

        if (!state.`is`(BlockTagLoader.TUMIDUSIO_HYPHA_REPLACEABLE)) {
            return false
        }

        if (CommonUtil.isNearFire(level, pos)) {
            return false
        }

        return true
    }

    private fun getExpandDirection(state: BlockState): Vec3i {
        val x = state.getValue(ModProperties.EXPAND_X) - 1
        val y = state.getValue(ModProperties.EXPAND_Y) - 1
        val z = state.getValue(ModProperties.EXPAND_Z) - 1
        return Vec3i(x, y, z)
    }

    private fun setExpandDirection(state: BlockState, direction: Vec3i): BlockState {
        val x = if(direction.x > 0) 2 else if(direction.x == 0) 1 else 0
        val y = if(direction.y > 0) 2 else if(direction.y == 0) 1 else 0
        val z = if(direction.z > 0) 2 else if(direction.z == 0) 1 else 0
        return state
            .setValue(ModProperties.EXPAND_X, x)
            .setValue(ModProperties.EXPAND_Y, y)
            .setValue(ModProperties.EXPAND_Z, z)
    }

    private fun angleOf(a: Vec3i, b: Vec3i): Double {
        val dot = a.dot(b)
        val aLen = sqrt((a.x * a.x + a.y * a.y + a.z * a.z).toDouble())
        val bLen = sqrt((b.x * b.x + b.y * b.y + b.z * b.z).toDouble())
        val cosAB = dot / (aLen * bLen)
        if(cosAB > 1) {
            return 0.0
        }
        if (cosAB < -1) {
            return PI
        }
        return acos(cosAB)
    }

    private fun genInitialExpandDirection(random: RandomSource): Vec3i {
        return INITIAL_EXPAND_DIRECTIONS[random.nextInt(INITIAL_EXPAND_DIRECTIONS.size)]
    }

    private fun genRandomExpandDirection(
        level: ServerLevel,
        pos: BlockPos,
        initialDirection: Vec3i,
        random: RandomSource
    ): Vec3i? {
        val availableDirections = arrayListOf<Vec3i>()
        for (d in EXPAND_DIRECTIONS) {
            val newPos = pos.offset(d)
            val newState = level.getBlockState(newPos)
            if(newState.`is`(BlockTagLoader.TUMIDUSIO_REPLACEABLE)) {
                availableDirections.add(d)
            }
            if(newState.`is`(BlockLoader.TUMIDUSIO.block)) {
                val density = newState.getValue(ModProperties.DENSITY)
                if(density < 32) {
                    availableDirections.add(d)
                }
            }
        }
        if(availableDirections.size == 0) {
            return null
        }

        val rates = arrayListOf(0.0)
        var sumRate = 0.0
        for (d in availableDirections) {
            val angle = angleOf(initialDirection, d)
            val adjustedAngle = angle + PI / 4
            val baseRate = 1 / adjustedAngle
            val len = sqrt((d.x * d.x + d.y * d.y + d.z * d.z).toDouble())
            val rate = baseRate / len
            rates.add(rate)
            sumRate += rate
        }
        for (i in rates.indices) {
            rates[i] /= sumRate
        }
        for(i in 1..<rates.size) {
            rates[i] += rates[i - 1]
        }

        val randomValue = random.nextDouble()
        for(i in 0..rates.size - 2) {
            if(randomValue >= rates[i] && randomValue < rates[i + 1]) {
                return availableDirections[i]
            }
        }
        return null
    }

    private fun randomSetState(
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
        direction: Vec3i,
        scheduleTick: Boolean
    ): Boolean {
        val state = level.getBlockState(pos)
        val stateDensity = state.getValue(ModProperties.DENSITY)
        if(stateDensity == 32) {
            return false
        }
        val randomValue = random.nextInt(stateDensity + 1)
        val newState = if (randomValue == 0) {
            setExpandDirection(state, direction)
        } else {
            state
        }.setValue(ModProperties.DENSITY, min(stateDensity + 1, 32))
        level.setBlock(pos, newState, 3)
        if(scheduleTick) {
            level.scheduleTick(pos, BlockLoader.TUMIDUSIO.block, 1)
        }
        return true
    }

    fun expand(level: ServerLevel, pos: BlockPos, state: BlockState, random: RandomSource) {
        val density = state.getValue(ModProperties.DENSITY)
        if(density <= 1) {
            return
        }

        var initialDirection = getExpandDirection(state)
        if(initialDirection.x == 0 && initialDirection.y == 0 && initialDirection.z == 0) {
            initialDirection = genInitialExpandDirection(random)
        }
        val testPos = pos.offset(initialDirection)
        val testState = level.getBlockState(testPos)
        if (!testState.`is`(BlockTagLoader.TUMIDUSIO_REPLACEABLE) && !testState.`is`(BlockLoader.TUMIDUSIO.block)) {
            initialDirection = -initialDirection
        }

        var successfulExpansions = 0
        for (i in 1..<density) {
            var newPos: BlockPos
            var oldPos = pos
            var newState: BlockState
            var finished = false
            for (j in 1..16) {
                val direction = genRandomExpandDirection(level, pos, initialDirection, random)
                if (direction == null) {
                    if(oldPos != pos) {
                        randomSetState(level, oldPos, random, initialDirection, false)
                        finished = true
                        successfulExpansions++
                    }
                    break
                }
                newPos = oldPos.offset(direction)
                newState = level.getBlockState(newPos)
                if (newState.`is`(BlockTagLoader.TUMIDUSIO_REPLACEABLE)) {
                    level.setBlock(newPos, BlockLoader.TUMIDUSIO.block.defaultBlockState(), 3)
                    finished = true
                    successfulExpansions++
                    break
                }
                if (!newState.`is`(BlockLoader.TUMIDUSIO.block) && oldPos != pos) {
                    val result = randomSetState(level, oldPos, random, direction, true)
                    finished = true
                    if(result) {
                        successfulExpansions++
                    }
                    break
                }
                oldPos = newPos
            }
            if(!finished && oldPos != pos) {
                val result = randomSetState(level, oldPos, random, initialDirection, true)
                if(result) {
                    successfulExpansions++
                }
            }
        }

        if (successfulExpansions > 0) {
            level.setBlock(pos, state.setValue(ModProperties.DENSITY, density - successfulExpansions), 3)
        }
    }

    fun expandHypha(level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if(random.nextFloat() < GrandisporiaUtil.INITIAL_GROW_RATE) {
            GrandisporiaUtil.tryGrowInitialStipe(level, pos.above())
        }

        val randomX = random.nextIntBetweenInclusive(-1, 1)
        val randomY = random.nextIntBetweenInclusive(-1, 1)
        val randomZ = random.nextIntBetweenInclusive(-1, 1)
        if(randomX == 0 && randomY == 0 && randomZ == 0) {
            return
        }
        val newPos = pos.offset(randomX, randomY, randomZ)
        if(XenolichenUtil.canGrow(level, newPos)) {
            XenolichenUtil.setXenolichen(level, newPos)
        } else if (MycovastusUtil.canHyphaGrow(level, newPos)) {
            MycovastusUtil.setHypha(level, newPos)
        }
    }
}