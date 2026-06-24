package dev.iebgames.iebclient.mixin;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.modules.render.Chams;
import dev.iebgames.iebclient.module.modules.render.ESP;
import dev.iebgames.iebclient.module.modules.render.Wallhack;
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
        if (IEBClient.moduleManager == null) return;

        ESP esp = IEBClient.moduleManager.getModule(ESP.class);
        Wallhack wallhack = IEBClient.moduleManager.getModule(Wallhack.class);
        Chams chams = IEBClient.moduleManager.getModule(Chams.class);

        if ((esp != null && esp.isEnabled()) || (wallhack != null && wallhack.isEnabled()) || (chams != null && chams.isEnabled())) {
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0f, -1000000.0f);
        }

        if (chams != null && chams.isEnabled() && entity != net.minecraft.client.Minecraft.getMinecraft().thePlayer) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1f, 0.2f, 0.2f, 0.55f);
        }
    }

    @Inject(method = "doRender", at = @At("RETURN"))
    private void onPostRender(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (IEBClient.moduleManager == null) return;

        ESP esp = IEBClient.moduleManager.getModule(ESP.class);
        Wallhack wallhack = IEBClient.moduleManager.getModule(Wallhack.class);
        Chams chams = IEBClient.moduleManager.getModule(Chams.class);

        if (chams != null && chams.isEnabled() && entity != net.minecraft.client.Minecraft.getMinecraft().thePlayer) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(1f, 1f, 1f, 1f);
        }

        if ((esp != null && esp.isEnabled()) || (wallhack != null && wallhack.isEnabled()) || (chams != null && chams.isEnabled())) {
            GL11.glPolygonOffset(1.0f, 1000000.0f);
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        }
    }
}
