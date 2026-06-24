package dev.iebgames.iebclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public final class EspRenderHelper {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private EspRenderHelper() {}

    public static void drawEntityBox(Entity entity, int color, float partialTicks) {
        if (entity == null || entity == mc.thePlayer) return;
        RenderUtils.draw3DBoxESP(entity, color, partialTicks);
    }

    public static void drawTracer(Entity entity, int color, float partialTicks) {
        if (entity == null || mc.thePlayer == null) return;

        double px = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        double py = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * partialTicks + mc.thePlayer.getEyeHeight() - mc.getRenderManager().viewerPosY;
        double pz = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

        double ex = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        double ey = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks + entity.height / 2 - mc.getRenderManager().viewerPosY;
        double ez = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        float a = ((color >> 24) & 0xFF) / 255f;
        if (a == 0) a = 1f;

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glLineWidth(1.5f);
        GL11.glColor4f(r, g, b, a);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(px, py, pz);
        GL11.glVertex3d(ex, ey, ez);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawBlockBox(double x, double y, double z, int color, float partialTicks) {
        if (mc.thePlayer == null) return;
        double vx = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * partialTicks;
        double vy = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * partialTicks;
        double vz = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * partialTicks;

        AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1)
                .offset(-vx, -vy, -vz);

        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        float a = ((color >> 24) & 0xFF) / 255f;
        if (a == 0) a = 0.5f;

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glLineWidth(1.5f);
        GL11.glColor4f(r, g, b, a);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void renderPlayers(int color, float partialTicks) {
        if (mc.theWorld == null) return;
        for (EntityPlayer p : mc.theWorld.playerEntities) {
            drawEntityBox(p, color, partialTicks);
        }
    }

    public static void renderMobs(int color, float partialTicks) {
        if (mc.theWorld == null) return;
        for (Object o : mc.theWorld.loadedEntityList) {
            if (o instanceof EntityMob) drawEntityBox((Entity) o, color, partialTicks);
        }
    }

    public static void renderItems(int color, float partialTicks) {
        if (mc.theWorld == null) return;
        for (Object o : mc.theWorld.loadedEntityList) {
            if (o instanceof EntityItem) drawEntityBox((Entity) o, color, partialTicks);
        }
    }

    public static void renderChests(int color, float partialTicks) {
        if (mc.theWorld == null) return;
        for (Object o : mc.theWorld.loadedTileEntityList) {
            if (o instanceof TileEntityChest || o instanceof TileEntityEnderChest) {
                TileEntity te = (TileEntity) o;
                drawBlockBox(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), color, partialTicks);
            }
        }
    }

    public static void renderStorage(int color, float partialTicks) {
        renderChests(color, partialTicks);
    }
}
