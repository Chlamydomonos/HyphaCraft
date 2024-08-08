package xyz.chlamydomonos.hyphacraft.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.chlamydomonos.hyphacraft.blocks.utils.BurnableHypha;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin {

    @Inject(
        method = "checkBurnOut",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"
        ),
        cancellable = true
    )
    private void injectCheckBurnoutRemove(
        Level level,
        BlockPos pos,
        int chance,
        RandomSource random,
        int age,
        Direction face,
        CallbackInfo callbackInfo
    ) {
        var state = level.getBlockState(pos);
        if(state.getBlock() instanceof BurnableHypha burnableHypha) {
            var result = burnableHypha.onBurnt(state, level, pos, false, random);
            if(result == BurnableHypha.VanillaBehaviourHandler.CANCEL) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(
        method = "checkBurnOut",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
        ),
        cancellable = true
    )
    private void injectCheckBurnoutSet(
        Level level,
        BlockPos pos,
        int chance,
        RandomSource random,
        int age,
        Direction face,
        CallbackInfo callbackInfo
    ) {
        var state = level.getBlockState(pos);
        if(state.getBlock() instanceof BurnableHypha burnableHypha) {
            var result = burnableHypha.onBurnt(state, level, pos, true, random);
            if(result == BurnableHypha.VanillaBehaviourHandler.CANCEL) {
                callbackInfo.cancel();
            }
        }
    }
}
