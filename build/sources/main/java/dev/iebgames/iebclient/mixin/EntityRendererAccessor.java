package dev.iebgames.iebclient.mixin;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityRenderer.class)
public interface EntityRendererAccessor {
    @Invoker("setupCameraTransform")
    void setupCameraTransformIEB(float partialTicks, int pass);
}
