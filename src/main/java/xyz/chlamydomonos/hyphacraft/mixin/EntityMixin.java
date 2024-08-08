package xyz.chlamydomonos.hyphacraft.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.chlamydomonos.hyphacraft.loaders.EntityLoader;

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
        var vehicle = this.getVehicle();
        while (vehicle != null) {
            if (vehicle.getType() == EntityLoader.INSTANCE.getTRANSPORT()) {
                callbackInfo.setReturnValue(false);
            }
            vehicle = vehicle.getVehicle();
        }
    }
}
