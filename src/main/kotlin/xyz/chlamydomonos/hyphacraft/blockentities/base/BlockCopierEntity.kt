package xyz.chlamydomonos.hyphacraft.blockentities.base

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.Connection
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
    var copiedState = Blocks.STONE.defaultBlockState()
        set(value) {
            field = value
            setChanged()
        }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        val copiedStateTag = NbtUtils.writeBlockState(copiedState)
        tag.put("copied_state", copiedStateTag)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)

        copiedState = NbtUtils.readBlockState(
            registries.lookupOrThrow(Registries.BLOCK),
            tag.getCompound("copied_state")
        )
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        val tag = super.getUpdateTag(registries)
        saveAdditional(tag, registries)
        return tag
    }

    override fun getUpdatePacket() = ClientboundBlockEntityDataPacket.create(this)

    override fun onDataPacket(
        net: Connection,
        pkt: ClientboundBlockEntityDataPacket,
        lookupProvider: HolderLookup.Provider
    ) {
        val oldState = copiedState
        super.onDataPacket(net, pkt, lookupProvider)
        if(copiedState.block != oldState.block) {
            level?.setBlocksDirty(blockPos, copiedState, blockState)
        }
    }
}