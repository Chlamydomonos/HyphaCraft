package xyz.chlamydomonos.hyphacraft.items

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import xyz.chlamydomonos.hyphacraft.loaders.BiomeLoader
import xyz.chlamydomonos.hyphacraft.loaders.DataAttachmentLoader

class BiomeDebugStickItem : Item(Properties()) {
    override fun useOn(context: UseOnContext): InteractionResult {
        if (context.level.isClientSide) {
            return InteractionResult.PASS
        }

        val level = context.level as ServerLevel
        val chunk = level.getChunkAt(context.clickedPos)
        if (!chunk.getData(DataAttachmentLoader.IS_ALIEN_FOREST)) {
            chunk.fillBiomesFromNoise(
                { _, _, _, _ -> BiomeLoader.ALIEN_FOREST(level) },
                level.chunkSource.randomState().sampler()
            )
            chunk.setData(DataAttachmentLoader.IS_ALIEN_FOREST, true)
            level.chunkSource.chunkMap.resendBiomesForChunks(listOf(chunk))
        }
        return InteractionResult.SUCCESS
    }
}