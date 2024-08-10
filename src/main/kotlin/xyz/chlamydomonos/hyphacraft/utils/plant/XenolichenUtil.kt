package xyz.chlamydomonos.hyphacraft.utils.plant

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.config.ModConfigEvent
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.blockentities.XenolichenBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.BlockTagLoader
import xyz.chlamydomonos.hyphacraft.loaders.ConfigLoader
import xyz.chlamydomonos.hyphacraft.utils.CommonUtil
import java.util.stream.Collectors

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
object XenolichenUtil {
    const val EXPAND_RATE = 1.0f / 6.0f
    private lateinit var XENOLICHEN_BLACKLIST: Set<Block>

    @SubscribeEvent
    fun onConfig(event: ModConfigEvent) {
        XENOLICHEN_BLACKLIST = ConfigLoader.XENOLICHEN_BLACKLIST.get().stream().map {
            BuiltInRegistries.BLOCK.get(ResourceLocation.parse(it))
        }.collect(Collectors.toSet())
    }

    fun canGrow(level: ServerLevel, pos: BlockPos): Boolean {
        if (!level.isLoaded(pos)) {
            return false
        }

        val state = level.getBlockState(pos)
        if(state.block in XENOLICHEN_BLACKLIST) {
            return false
        }

        if (!state.`is`(BlockTagLoader.XENOLICHEN_REPLACEABLE)) {
            return false
        }

        var hasLight = false
        for(direction in Direction.stream()) {
            val neighbor = pos.offset(direction.normal)
            if(level.isLoaded(neighbor)) {
                if (level.getRawBrightness(neighbor, 0) >= 8) {
                    hasLight = true
                    break
                }
            }
        }
        if (!hasLight) {
            return false
        }

        if (CommonUtil.isNearFire(level, pos)) {
            return false
        }

        return true
    }

    fun setXenolichen(level: ServerLevel, pos: BlockPos, phase: Int = 0) {
        val state = level.getBlockState(pos)
        val oldBe = level.getBlockEntity(pos)
        var oldCopiedState: BlockState? = null
        if(oldBe is XenolichenBlockEntity) {
            oldCopiedState = oldBe.copiedState
        }

        level.setBlock(
            pos,
            BlockLoader.XENOLICHEN_BLOCK.defaultBlockState().setValue(ModProperties.PHASE, phase),
            3
        )
        val be = level.getBlockEntity(pos) as XenolichenBlockEntity
        be.copiedState = oldCopiedState ?: state
    }
}