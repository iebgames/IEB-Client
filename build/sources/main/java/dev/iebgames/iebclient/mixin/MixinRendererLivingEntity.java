package dev.iebgames.iebclient.mixin;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.modules.render.ESP;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> {

    @Inject(method = "doRender", at = @At("HEAD"))
    private void onPreRender(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        ESP esp = (ESP) IEBClient.INSTANCE.moduleManager.getModule("ESP");
        if (esp != null && esp.isEnabled()) {
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0f, -1000000.0f);
        }
    }

    @Inject(method = "doRender", at = @At("RETURN"))
    private void onPostRender(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        ESP esp = (ESP) IEBClient.INSTANCE.moduleManager.getModule("ESP");
        if (esp != null && esp.isEnabled()) {
            GL11.glPolygonOffset(1.0f, 1000000.0f);
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        }
    }
}
