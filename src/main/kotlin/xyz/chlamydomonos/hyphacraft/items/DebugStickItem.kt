package xyz.chlamydomonos.hyphacraft.items

import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.plus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import xyz.chlamydomonos.hyphacraft.datacomponents.BlockHolder
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader
import xyz.chlamydomonos.hyphacraft.loaders.DataComponentLoader
import xyz.chlamydomonos.hyphacraft.loaders.EntityLoader

class DebugStickItem : Item(
    Properties().component(DataComponentLoader.BLOCK_HOLDER, BlockHolder(BlockLoader.XENOLICHEN_BLOCK))
) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        if (level.isClientSide) {
            return InteractionResult.PASS
        }

        val entity = EntityLoader.SPORE_CLOUD.create(level)!!
        entity.setPos(context.clickedPos.toVec3() + Vec3(0.0, 2.0, 0.0))
        entity.range = 2.0f
        entity.ticksLeft = 5
        level.addFreshEntity(entity)

        return InteractionResult.SUCCESS
    }
}