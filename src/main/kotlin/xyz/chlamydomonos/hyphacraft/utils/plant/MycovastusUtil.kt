package xyz.chlamydomonos.hyphacraft.utils.plant

import net.minecraft.core.BlockPos
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
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.blockentities.MycovastusHyphaBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.BlockTagLoader
import xyz.chlamydomonos.hyphacraft.loaders.ConfigLoader
import xyz.chlamydomonos.hyphacraft.utils.CommonUtil
import java.util.stream.Collectors
import kotlin.math.pow

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
object MycovastusUtil {
    const val EXPAND_RATE = 1.0f / 3.0f
    const val MUSHROOM_RATE = 1.0f / 20.0f
    private lateinit var MYCOVASTUS_BLACKLIST: Set<Block>

    @SubscribeEvent
    fun onConfig(event: ModConfigEvent) {
        MYCOVASTUS_BLACKLIST = ConfigLoader.MYCOVASTUS_BLACKLIST.get().stream().map {
            BuiltInRegistries.BLOCK.get(ResourceLocation.parse(it))
        }.collect(Collectors.toSet())
    }
    fun canHyphaGrow(level: LevelReader, pos: BlockPos): Boolean {
        if (level is ServerLevel && !level.isLoaded(pos)) {
            return false
        }

        val state = level.getBlockState(pos)
        if(state.block in MYCOVASTUS_BLACKLIST) {
            return false
        }

        if (!state.`is`(BlockTagLoader.MYCOVASTUS_HYPHA_REPLACEABLE)) {
            return false
        }

        if (CommonUtil.isNearFire(level, pos)) {
            return false
        }

        return true
    }

    fun setHypha(level: ServerLevel, pos: BlockPos, phase: Int = 0) {
        val state = level.getBlockState(pos)
        val oldBe = level.getBlockEntity(pos)
        var oldCopiedState: BlockState? = null
        if(oldBe is MycovastusHyphaBlockEntity) {
            oldCopiedState = oldBe.copiedState
        }

        level.setBlock(
            pos,
            BlockLoader.MYCOVASTUS_HYPHA.defaultBlockState().setValue(ModProperties.PHASE, phase),
            3
        )
        val be = level.getBlockEntity(pos) as MycovastusHyphaBlockEntity
        be.copiedState = oldCopiedState ?: state
    }

    fun growMushroom(level: ServerLevel, pos: BlockPos, random: RandomSource) {
        var hyphaCount = 0
        var testPos = pos
        while (level.getBlockState(testPos).`is`(BlockLoader.MYCOVASTUS_HYPHA)) {
            hyphaCount++
            testPos = testPos.below()
        }

        val randomNum = random.nextDouble().pow(1.0 / hyphaCount)
        val state = BlockLoader.MYCOVASTUS.block.defaultBlockState()
        if(randomNum < 1.0f / 3.0f) {
            level.setBlock(pos.above(), state.setValue(ModProperties.MUSHROOM_COUNT, 1), 3)
        } else if(randomNum < 2.0f / 3.0f) {
            level.setBlock(pos.above(), state.setValue(ModProperties.MUSHROOM_COUNT, 2), 3)
        } else {
            level.setBlock(pos.above(), state.setValue(ModProperties.MUSHROOM_COUNT, 3), 3)
        }
    }
}