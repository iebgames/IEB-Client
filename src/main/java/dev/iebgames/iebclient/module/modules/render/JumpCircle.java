package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventRender3D;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class JumpCircle extends Module {

    private final List<Circle> circles = new ArrayList<>();

    public JumpCircle() {
        super("JumpCircle", "Zıpladığında yerde halka efekti oluşturur.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.onGround && mc.gameSettings.keyBindJump.isPressed()) {
            circles.add(new Circle(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
        }
        circles.removeIf(c -> c.age > 20);
        for (Circle c : circles) c.age++;
    }

    @EventHook
    public void onRender3D(EventRender3D e) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glLineWidth(2.0f);

        for (Circle c : circles) {
            double x = c.x - mc.getRenderManager().viewerPosX;
            double y = c.y - mc.getRenderManager().viewerPosY;
            double z = c.z - mc.getRenderManager().viewerPosZ;
            float radius = c.age * 0.1f;
            float alpha = 1.0f - (c.age / 20.0f);

            GL11.glColor4f(1, 1, 1, alpha);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            for (int i = 0; i <= 360; i += 10) {
                GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * radius, y, z + Math.sin(Math.toRadians(i)) * radius);
            }
            GL11.glEnd();
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    private static class Circle {
        double x, y, z;
        int age = 0;
        Circle(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
    }
}
