package dev.iebgames.iebclient.mixin;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.modules.render.NoHurtCam;
import dev.iebgames.iebclient.module.modules.world.BlockReach;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

    @Inject(method = "getBlockReachDistance", at = @At("RETURN"), cancellable = true)
    private void onGetBlockReachDistance(CallbackInfoReturnable<Float> cir) {
        if (IEBClient.moduleManager == null) return;
        BlockReach br = IEBClient.moduleManager.getModule(BlockReach.class);
        if (br != null && br.isEnabled()) {
            cir.setReturnValue(br.distance.getFloat());
        }
    }
}
