package xyz.chlamydomonos.hyphacraft.render.block

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.ChunkRenderTypeSet
import net.neoforged.neoforge.client.model.data.ModelData
import net.neoforged.neoforge.client.model.data.ModelProperty
import xyz.chlamydomonos.hyphacraft.blockentities.BlockCopierEntity

open class BlockCopierModel(private val customModel: BakedModel) : BakedModel by customModel {
    companion object {
        val COPIED_MODEL = ModelProperty<BakedModel>()
        val COPIED_STATE = ModelProperty<BlockState>()
    }

    override fun getQuads(
        state: BlockState?,
        side: Direction?,
        rand: RandomSource,
        data: ModelData,
        renderType: RenderType?
    ): MutableList<BakedQuad> {
        val copiedModel = data.get(COPIED_MODEL)
        if (copiedModel is BakedModel) {
            val copiedState = data.get(COPIED_STATE)!!
            val quads = arrayListOf<BakedQuad>()
            quads.addAll(copiedModel.getQuads(copiedState, side, rand, data, renderType))
            if (renderType == RenderType.TRANSLUCENT) {
                quads.addAll(customModel.getQuads(state, side, rand, data, renderType))
            }
            return quads
        }
        return mutableListOf()
    }

    @Deprecated("Deprecated in Java")
    override fun getQuads(p0: BlockState?, p1: Direction?, p2: RandomSource): MutableList<BakedQuad> {
        throw AssertionError("Deprecated")
    }

    override fun getModelData(
        level: BlockAndTintGetter,
        pos: BlockPos,
        state: BlockState,
        modelData: ModelData
    ): ModelData {
        val blockEntity = level.getBlockEntity(pos)
        if (blockEntity is BlockCopierEntity) {
            val copiedState = blockEntity.copiedState
            val copiedModel = Minecraft.getInstance().blockRenderer.getBlockModel(copiedState)
            return modelData.derive().with(COPIED_MODEL, copiedModel).with(COPIED_STATE, copiedState).build()
        }
        return modelData
    }

    override fun getRenderTypes(state: BlockState, rand: RandomSource, data: ModelData): ChunkRenderTypeSet {
        val superTypes = super.getRenderTypes(state, rand, data)
        return ChunkRenderTypeSet.union(superTypes, ChunkRenderTypeSet.of(RenderType.TRANSLUCENT))
    }
}