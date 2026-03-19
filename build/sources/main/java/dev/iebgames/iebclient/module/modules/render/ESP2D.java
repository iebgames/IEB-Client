package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventRender3D;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ESP2D extends Module {

    public ESP2D() {
        super("ESP2D", "Oyuncuların etrafına 2 boyutlu kutu çizer.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onRender3D(EventRender3D e) {
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer) continue;

            // Simplified 2D EPS would require project to screen coords
            // For now, we draw a 3D billboarded box as a 2D substitute
            double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * e.getPartialTicks() - mc.getRenderManager().viewerPosX;
            double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * e.getPartialTicks() - mc.getRenderManager().viewerPosY;
            double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * e.getPartialTicks() - mc.getRenderManager().viewerPosZ;

            GL11.glPushMatrix();
            GL11.glTranslated(x, y + player.getEyeHeight() / 2, z);
            GL11.glRotated(-mc.getRenderManager().playerViewY, 0, 1, 0);
            
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glColor4f(1, 1, 1, 1);
            
            // Draw a simple 2D rect facing the player
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex2d(-0.5, -1.0);
            GL11.glVertex2d(0.5, -1.0);
            GL11.glVertex2d(0.5, 1.0);
            GL11.glVertex2d(-0.5, 1.0);
            GL11.glEnd();

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glPopMatrix();
        }
    }
}
