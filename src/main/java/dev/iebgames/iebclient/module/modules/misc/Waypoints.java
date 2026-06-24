package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventRender2D;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.TextSetting;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class Waypoints extends Module {

    private final TextSetting add = register(new TextSetting("Add", ""));
    private final List<double[]> points = new ArrayList<>();

    public Waypoints() {
        super("Waypoints", "Koordinat işaretleri ve yön göstergesi.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onRender2D(EventRender2D e) {
        if (mc.thePlayer == null) return;
        String raw = add.getValue();
        if (raw != null && raw.contains(",")) {
            try {
                String[] p = raw.split(",");
                points.add(new double[]{Double.parseDouble(p[0].trim()), Double.parseDouble(p[1].trim()), Double.parseDouble(p[2].trim())});
                add.setValue("");
            } catch (Exception ignored) {}
        }

        int y = 60;
        for (double[] pt : points) {
            double dx = pt[0] - mc.thePlayer.posX;
            double dz = pt[2] - mc.thePlayer.posZ;
            double dist = Math.sqrt(dx * dx + dz * dz);
            float yawDiff = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90 - mc.thePlayer.rotationYaw);
            mc.fontRendererObj.drawStringWithShadow(String.format("WP %.0f,%.0f,%.0f | %.1fm | %.0f°", pt[0], pt[1], pt[2], dist, yawDiff), 4, y, 0xFFFFFF55);
            y += 10;
        }
    }
}
