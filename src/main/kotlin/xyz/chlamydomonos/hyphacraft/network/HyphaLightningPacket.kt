package xyz.chlamydomonos.hyphacraft.network

import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.handling.IPayloadContext
import org.joml.Vector3f
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.div
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.minus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.plus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.times

class HyphaLightningPacket : BasePacket<HyphaLightningPacket.Data> (
    "hypha_lightning",
    Direction.TO_CLIENT,
    Phase.PLAY
) {
    companion object {
        const val SEGMENTS_PER_METER = 8
        const val PARTICLES_PER_SEGMENT = 16
    }

    class Data {
        var from = Vector3f()
        var to = Vector3f()
    }

    override val factory = ::Data
    override val codec = codec(vector3f(Data::from).vector3f(Data::to))

    override fun onClientReceived(packet: Data, context: IPayloadContext) {
        context.enqueueWork {
            val level = context.player().level()
            val distance = packet.from.distance(packet.to)
            val segmentCount = (distance * SEGMENTS_PER_METER).toInt()
            val segmentLen = 1.0 / SEGMENTS_PER_METER

            val from = Vec3(packet.from)
            val to = Vec3(packet.to)
            val delta = ((to - from) / distance.toDouble()) * segmentLen
            val segments = arrayListOf<Vec3>()
            segments.add(from)
            for (i in 1..<segmentCount) {
                val pos = from + (delta * i.toDouble()) + Vec3(
                    level.random.nextGaussian() * segmentLen,
                    level.random.nextGaussian() * segmentLen,
                    level.random.nextGaussian() * segmentLen
                )
                segments.add(pos)
            }
            segments.add(to)

            for (i in 0..<segmentCount) {
                val pos1 = segments[i]
                val pos2 = segments[i + 1]
                for (j in 0..PARTICLES_PER_SEGMENT) {
                    val rate = j.toDouble() / PARTICLES_PER_SEGMENT
                    val actualPos = pos1 * rate + pos2 * (1 - rate)
                    level.addParticle(ParticleTypes.ELECTRIC_SPARK, actualPos.x, actualPos.y, actualPos.z, 0.0, 0.0, 0.0)
                }
            }
        }
    }
}