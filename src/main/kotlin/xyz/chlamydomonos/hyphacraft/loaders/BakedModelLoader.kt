package xyz.chlamydomonos.hyphacraft.loaders

import net.minecraft.client.renderer.block.BlockModelShaper
import net.minecraft.world.level.block.Block
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ModelEvent
import xyz.chlamydomonos.hyphacraft.HyphaCraft
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.render.block.BlockCopierModel

@EventBusSubscriber(modid = HyphaCraft.MODID, bus = EventBusSubscriber.Bus.MOD)
object BakedModelLoader {
    private fun ModelEvent.ModifyBakingResult.registerBlockCopier(
        block: Block,
        hiddenBlock: Block
    ) {
        for(state in block.stateDefinition.possibleStates) {
            val property = ModProperties.PHASE
            val phase = state.getValue(property)
            val newState = hiddenBlock.defaultBlockState().setValue(property, phase)
            val oldLocation = BlockModelShaper.stateToModelLocation(state)
            val oldModel = models[oldLocation]
            if(oldModel is BlockCopierModel) {
                throw RuntimeException("WTF")
            }
            val newLocation = BlockModelShaper.stateToModelLocation(newState)
            val newModel = BlockCopierModel(models[newLocation]!!)
            models[oldLocation] = newModel
        }
    }

    @SubscribeEvent
    fun onModelBaked(event: ModelEvent.ModifyBakingResult) {
        event.registerBlockCopier(BlockLoader.XENOLICHEN_BLOCK, BlockLoader.XENOLICHEN_HIDDEN_BLOCK)
        event.registerBlockCopier(BlockLoader.MYCOVASTUS_HYPHA, BlockLoader.MYCOVASTUS_HYPHA_HIDDEN_BLOCK)
        event.registerBlockCopier(BlockLoader.TUMIDUSIO_HYPHA, BlockLoader.TUMIDUSIO_HYPHA_HIDDEN_BLOCK)
    }
}