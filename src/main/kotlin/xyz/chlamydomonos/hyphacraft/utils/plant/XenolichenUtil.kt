package xyz.chlamydomonos.hyphacraft.utils.plant

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.config.ModConfigEvent
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.blockentities.XenolichenBlockEntity
import xyz.chlamydomonos.hyphacraft.blocks.utils.HyphaCraftProperties
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.ConfigLoader
import java.util.stream.Collectors

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
object XenolichenUtil {
    const val EXPAND_RATE = 1.0f / 6.0f

    private lateinit var XENOLICHEN_TAGS: Set<TagKey<Block>>
    private lateinit var XENOLICHEN_BLOCKS: Set<Block>
    private lateinit var XENOLICHEN_BLACKLIST: Set<Block>

    @SubscribeEvent
    fun onConfig(event: ModConfigEvent) {
        XENOLICHEN_TAGS = ConfigLoader.XENOLICHEN_TAGS.get().stream().map {
            TagKey.create(Registries.BLOCK, ResourceLocation.parse(it))
        }.collect(Collectors.toSet())

        XENOLICHEN_BLOCKS = ConfigLoader.XENOLICHEN_BLOCKS.get().stream().map {
            BuiltInRegistries.BLOCK.get(ResourceLocation.parse(it))
        }.collect(Collectors.toSet())

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

        var isGrowableBlock = false
        if(state.block in XENOLICHEN_BLOCKS) {
            isGrowableBlock = true
        }

        if (!isGrowableBlock) {
            for (tag in XENOLICHEN_TAGS) {
                if (state.`is`(tag)) {
                    isGrowableBlock = true
                }
            }
        }

        if(!isGrowableBlock) {
            return false
        }

        var hasLight = false
        for(direction in Direction.stream()) {
            val neighbor = pos.offset(direction.normal)
            if(level.isLoaded(neighbor)) {
                if (level.getRawBrightness(neighbor, 0) >= 8) {
                    hasLight = true
                }
            }
        }
        return hasLight
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
            BlockLoader.XENOLICHEN_BLOCK.defaultBlockState().setValue(HyphaCraftProperties.PHASE, phase),
            3
        )
        val be = level.getBlockEntity(pos) as XenolichenBlockEntity
        be.copiedState = oldCopiedState ?: state
    }
}