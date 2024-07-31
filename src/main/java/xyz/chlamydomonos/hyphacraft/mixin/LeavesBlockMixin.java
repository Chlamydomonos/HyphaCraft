package xyz.chlamydomonos.hyphacraft.mixin;

import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.chlamydomonos.hyphacraft.blocks.utils.ModBlockTags;

import java.util.OptionalInt;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {
    @Inject(
        method = "getOptionalDistanceAt",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private static void injectGetOptionalDistanceAt(BlockState state, CallbackInfoReturnable<OptionalInt> callbackInfo) {
        if(state.is(ModBlockTags.INSTANCE.getPRESERVE_LEAVES())) {
            callbackInfo.setReturnValue(OptionalInt.of(0));
        }
    }
}
