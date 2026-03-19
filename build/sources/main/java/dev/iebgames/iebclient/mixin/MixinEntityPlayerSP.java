package dev.iebgames.iebclient.mixin;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.event.events.EventMotion;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    private void onPreMotionUpdate(CallbackInfo ci) {
        EntityPlayerSP player = (EntityPlayerSP) (Object) this;
        EventMotion event = new EventMotion(player.rotationYaw, player.rotationPitch, player.posX, player.getEntityBoundingBox().minY, player.posZ, player.onGround, true);
        IEBClient.INSTANCE.eventBus.post(event);
        
        // Apply spoofed rotations/position
        // player.rotationYaw = event.getYaw();
        // player.rotationPitch = event.getPitch();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"))
    private void onPostMotionUpdate(CallbackInfo ci) {
        EntityPlayerSP player = (EntityPlayerSP) (Object) this;
        IEBClient.INSTANCE.eventBus.post(new EventMotion(player.rotationYaw, player.rotationPitch, player.posX, player.getEntityBoundingBox().minY, player.posZ, player.onGround, false));
    }
}
