package xyz.chlamydomonos.hyphacraft.entity.goals

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal
import net.minecraft.world.level.LevelReader
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModProperties
import xyz.chlamydomonos.hyphacraft.entity.entities.HumifossorEntity
import xyz.chlamydomonos.hyphacraft.loaders.BlockLoader

class HumifossorChargeGoal(
    val entity: HumifossorEntity
) : MoveToBlockGoal(entity, 1.0, 10) {
    override fun isValidTarget(level: LevelReader, pos: BlockPos): Boolean {
        val state = level.getBlockState(pos)
        if (!state.`is`(BlockLoader.FULGURFUNGUS.block)) {
            return false
        }
        return state.getValue(ModProperties.ACTIVE)
    }

    override fun canUse(): Boolean {
        if (entity.charged) {
            return false
        }
        return super.canUse()
    }

    override fun canContinueToUse(): Boolean {
        if (entity.charged) {
            return false
        }
        return super.canContinueToUse()
    }
}