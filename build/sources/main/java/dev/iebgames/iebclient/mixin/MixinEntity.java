package dev.iebgames.iebclient.mixin;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.modules.combat.Hitbox;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(method = "getCollisionBorderSize", at = @At("HEAD"), cancellable = true)
    private void onGetCollisionBorderSize(CallbackInfoReturnable<Float> cir) {
        if (IEBClient.moduleManager == null) return;
        Hitbox hitbox = IEBClient.moduleManager.getModule(Hitbox.class);
        if (hitbox != null && hitbox.isEnabled() && (Object) this != net.minecraft.client.Minecraft.getMinecraft().thePlayer) {
            cir.setReturnValue((float) hitbox.getExpansion());
        }
    }
}
