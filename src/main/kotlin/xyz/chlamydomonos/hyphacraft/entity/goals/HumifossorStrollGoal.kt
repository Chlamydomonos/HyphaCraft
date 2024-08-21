package xyz.chlamydomonos.hyphacraft.entity.goals

import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import xyz.chlamydomonos.hyphacraft.entity.entities.HumifossorEntity

class HumifossorStrollGoal(
    private val humifossor: HumifossorEntity
) : WaterAvoidingRandomStrollGoal(humifossor, 1.0) {
    override fun start() {
        super.start()
    }

    override fun stop() {
        super.stop()
    }
}