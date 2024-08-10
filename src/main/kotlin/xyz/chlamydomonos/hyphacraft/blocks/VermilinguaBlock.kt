package xyz.chlamydomonos.hyphacraft.blocks

import net.minecraft.core.BlockPos
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.ModelProvider
import xyz.chlamydomonos.hyphacraft.blocks.base.BaseHyphaBlock
import xyz.chlamydomonos.hyphacraft.datagen.ModBlockStateProvider
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.utils.NameUtil

class VermilinguaBlock : BaseHyphaBlock(
    Properties.ofFullCopy(Blocks.DIRT).noCollission().noOcclusion().sound(SoundType.SLIME_BLOCK).noLootTable()
) {
    companion object {
        fun genModel(provider: ModBlockStateProvider) {
            val name = NameUtil.path(BlockLoader.VERMILINGUA)
            val model = provider.models().cross(name, NameUtil.getRL("${ModelProvider.BLOCK_FOLDER}/$name"))
                .renderType("cutout")
            provider.simpleBlock(BlockLoader.VERMILINGUA, model)
        }
    }

    override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
        if (level.isClientSide) {
            return
        }

        if (entity is LivingEntity && level.random.nextInt(200) == 0) {
            val effect = MobEffectInstance(MobEffects.POISON, 50, 0)
            entity.addEffect(effect)
        }
    }
}