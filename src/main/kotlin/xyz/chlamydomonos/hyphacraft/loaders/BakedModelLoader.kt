package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.client.renderer.block.BlockModelShaper
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ModelEvent
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.blocks.utils.HyphaCraftProperties
import xyz.chlamydomonos.hyphacraft.render.block.BlockCopierModel
import xyz.chlamydomonos.hyphacraft.render.block.*

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
object BakedModelLoader {
    private fun ModelEvent.ModifyBakingResult.registerBlockCopier(
        block: Block,
        model: (BakedModel) -> BlockCopierModel,
        stateGenerator: (BlockState) -> BlockState?
    ) {
        for(state in block.stateDefinition.possibleStates) {
            val newState = stateGenerator(state) ?: continue
            val oldLocation = BlockModelShaper.stateToModelLocation(state)
            val oldModel = models[oldLocation]
            if(oldModel is BlockCopierModel) {
                throw RuntimeException("WTF")
            }
            val newLocation = BlockModelShaper.stateToModelLocation(newState)
            val newModel = model(models[newLocation]!!)
            models[oldLocation] = newModel
        }
    }

    @SubscribeEvent
    fun onModelBaked(event: ModelEvent.ModifyBakingResult) {
        event.registerBlockCopier(BlockLoader.XENOLICHEN_BLOCK, ::XenolichenBakedModel) {
            val property = HyphaCraftProperties.PHASE
            val phase = it.getValue(property)
            BlockLoader.XENOLICHEN_HIDDEN_BLOCK.defaultBlockState().setValue(property, phase)
        }
        event.registerBlockCopier(BlockLoader.MYCOVASTUS_HYPHA, ::MycovastusHyphaBakedModel) {
            val property = HyphaCraftProperties.PHASE
            val phase = it.getValue(property)
            BlockLoader.MYCOVASTUS_HYPHA_HIDDEN_BLOCK.defaultBlockState().setValue(property, phase)
        }
    }
}