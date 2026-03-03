package dev.iebgames.iebclient.mixin;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.event.events.EventKey;
import dev.iebgames.iebclient.event.events.EventUpdate;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(method = "runTick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        IEBClient.INSTANCE.eventBus.post(new EventUpdate());
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V"))
    private void onKey(CallbackInfo ci) {
        if (Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
            IEBClient.INSTANCE.eventBus.post(new EventKey(key));
        }
    }
}
