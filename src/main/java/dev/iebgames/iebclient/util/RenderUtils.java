package dev.iebgames.iebclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public final class RenderUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void draw2DRect(int x, int y, int w, int h, int color) {
        net.minecraft.client.gui.Gui.drawRect(x, y, x + w, y + h, color);
    }

    public static void drawBorderedRect(int x, int y, int w, int h, int color, int border, int borderColor) {
        draw2DRect(x - border, y - border, w + border * 2, h + border * 2, borderColor);
        draw2DRect(x, y, w, h, color);
    }

    public static void draw3DBoxESP(Entity entity, int color, float partialTicks) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks
                 - mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks
                 - mc.getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks
                 - mc.getRenderManager().viewerPosZ;

        AxisAlignedBB bb = entity.getEntityBoundingBox()
            .offset(-entity.posX, -entity.posY, -entity.posZ)
            .offset(x, y, z);

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(1.5f);
        GL11.glColor4f(((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f,
                       (color & 0xFF) / 255f, ((color >> 24) & 0xFF) / 255f);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ); GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ); GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ); GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ); GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ); GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ); GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ); GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ); GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
