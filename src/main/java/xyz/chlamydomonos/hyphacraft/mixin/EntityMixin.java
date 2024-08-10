package xyz.chlamydomonos.hyphacraft.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.chlamydomonos.hyphacraft.entity.entities.TransportEntity;

import javax.annotation.Nullable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow @Nullable public abstract Entity getVehicle();

    @Inject(
        method = "isInWall",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private void injectIsInWall(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (TransportEntity.Companion.isOnTransport(this.getVehicle())) {
            callbackInfo.setReturnValue(false);
        }
    }
}
