package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventRender3D;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class Projectiles extends Module {

    public Projectiles() {
        super("Projectiles", "Ok ve inci yolunu gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onRender3D(EventRender3D e) {
        ItemStack stack = mc.thePlayer.getHeldItem();
        if (stack == null) return;
        if (!(stack.getItem() instanceof ItemBow || stack.getItem() instanceof ItemEnderPearl || stack.getItem() instanceof ItemPotion)) return;

        double x = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * e.getPartialTicks();
        double y = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * e.getPartialTicks() + mc.thePlayer.getEyeHeight();
        double z = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * e.getPartialTicks();

        // Simplified trajectory calculation logic goes here
        // For now, we draw a basic line forward
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glLineWidth(2.0f);
        GL11.glColor4f(0, 1, 0, 0.5f);

        GL11.glBegin(GL11.GL_LINE_STRIP);
        // Trajectory would be points in world space
        GL11.glEnd();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
}
