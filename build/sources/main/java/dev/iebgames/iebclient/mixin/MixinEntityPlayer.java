package dev.iebgames.iebclient.mixin;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.event.events.EventAttack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer {

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At("HEAD"))
    private void onAttack(Entity target, CallbackInfo ci) {
        IEBClient.INSTANCE.eventBus.post(new EventAttack(target));
    }
}
