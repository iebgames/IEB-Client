package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventRender3D;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

public class TNTTimer extends Module {

    private final DecimalFormat format = new DecimalFormat("0.0");

    public TNTTimer() {
        super("TNTTimer", "Aktif TNT'lerin patlama süresini gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onRender3D(EventRender3D e) {
        for (Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityTNTPrimed) {
                EntityTNTPrimed tnt = (EntityTNTPrimed) obj;
                double x = tnt.lastTickPosX + (tnt.posX - tnt.lastTickPosX) * e.getPartialTicks() - mc.getRenderManager().viewerPosX;
                double y = tnt.lastTickPosY + (tnt.posY - tnt.lastTickPosY) * e.getPartialTicks() - mc.getRenderManager().viewerPosY + 0.5;
                double z = tnt.lastTickPosZ + (tnt.posZ - tnt.lastTickPosZ) * e.getPartialTicks() - mc.getRenderManager().viewerPosZ;

                float timer = tnt.fuse / 20.0F;
                String text = format.format(timer) + "s";
                
                drawTag(text, x, y, z);
            }
        }
    }

    private void drawTag(String text, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y + 1.2, z);
        GL11.glRotated(-mc.getRenderManager().playerViewY, 0, 1, 0);
        GL11.glRotated(mc.getRenderManager().playerViewX, 1, 0, 0);
        GL11.glScaled(-0.02, -0.02, 0.02);
        
        mc.fontRendererObj.drawStringWithShadow(text, -mc.fontRendererObj.getStringWidth(text) / 2, 0, -1);
        
        GL11.glPopMatrix();
    }
}
