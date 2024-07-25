package xyz.chlamydomonos.hyphacraft.blockentities

import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

open class BlockCopierEntity(
    type: BlockEntityType<*>,
    pos: BlockPos,
    blockState: BlockState
) : BlockEntity(type, pos, blockState) {
    var copiedState = Blocks.AIR.defaultBlockState()

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        val copiedStateTag = NbtUtils.writeBlockState(copiedState)
        tag.put("copied_state", copiedStateTag)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        copiedState = NbtUtils.readBlockState(
            Minecraft.getInstance().level!!.holderLookup(Registries.BLOCK),
            tag.getCompound("copied_state")
        )
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        val tag = super.getUpdateTag(registries)
        saveAdditional(tag, registries)
        return tag
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener>? = ClientboundBlockEntityDataPacket.create(this)
}