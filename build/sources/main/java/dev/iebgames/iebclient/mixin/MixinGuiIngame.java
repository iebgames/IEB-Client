package dev.iebgames.iebclient.mixin;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.event.events.EventRender2D;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Inject(method = "renderGameOverlay", at = @At("RETURN"))
    private void onRenderOverlay(float partialTicks, CallbackInfo ci) {
        IEBClient.eventBus.post(new EventRender2D());
    }

    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    private void onRenderTooltip(net.minecraft.client.gui.ScaledResolution sr, float partialTicks, CallbackInfo ci) {
        dev.iebgames.iebclient.module.modules.render.Crosshair ch = IEBClient.moduleManager.getModule(dev.iebgames.iebclient.module.modules.render.Crosshair.class);
        if (ch != null && ch.isToggled()) {
            // In 1.8.9, crosshair is drawn in renderGameOverlay usually, 
            // but some versions/setups have it in renderTooltip.
            // If we draw our own in HUD (which is called at RETURN of renderGameOverlay), 
            // we might need to cancel the vanilla one.
        }
    }
}
