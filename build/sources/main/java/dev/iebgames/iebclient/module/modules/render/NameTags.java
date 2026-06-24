package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventRender3D;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public class NameTags extends Module {

    private final NumberSetting scale = register(new NumberSetting("Scale", 1.5, 0.5, 3.0, 0.1));

    public NameTags() {
        super("NameTags", "Büyük isim etiketleri gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onRender3D(EventRender3D e) {
        if (mc.theWorld == null) return;
        for (EntityPlayer p : mc.theWorld.playerEntities) {
            if (p == mc.thePlayer || p.isInvisible()) continue;
            double x = p.lastTickPosX + (p.posX - p.lastTickPosX) * e.getPartialTicks() - mc.getRenderManager().viewerPosX;
            double y = p.lastTickPosY + (p.posY - p.lastTickPosY) * e.getPartialTicks() - mc.getRenderManager().viewerPosY + p.height + 0.5;
            double z = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * e.getPartialTicks() - mc.getRenderManager().viewerPosZ;

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0, 1, 0);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, 1, 0, 0);
            float s = scale.getFloat() * 0.025f;
            GlStateManager.scale(-s, -s, s);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            mc.fontRendererObj.drawStringWithShadow(p.getName(), -mc.fontRendererObj.getStringWidth(p.getName()) / 2f, 0, 0xFFFFFFFF);
            GlStateManager.depthMask(true);
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
}
