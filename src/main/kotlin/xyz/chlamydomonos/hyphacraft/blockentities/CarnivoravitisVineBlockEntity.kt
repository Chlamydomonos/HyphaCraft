package xyz.chlamydomonos.hyphacraft.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.world.level.block.state.BlockState
import xyz.chlamydomonos.hyphacraft.blockentities.base.RouteBlockEntity
import xyz.chlamydomonos.hyphacraft.loaders.BlockEntityLoader

class CarnivoravitisVineBlockEntity(
    pos: BlockPos,
    blockState: BlockState
) : RouteBlockEntity(BlockEntityLoader.CARNIVORAVITIS_VINE, pos, blockState) {
    var nextPos = pos
        set(value) {
            field = value
            setChanged()
        }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.put("next_pos", NbtUtils.writeBlockPos(nextPos))
    }

    override fun getNextPos(data: String) = nextPos

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        NbtUtils.readBlockPos(tag, "next_pos").ifPresent { nextPos = it }
    }
}