package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventRender2D;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import dev.iebgames.iebclient.util.RenderUtils;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public class Radar extends Module {

    private final NumberSetting size = register(new NumberSetting("Size", 60, 40, 120, 5));
    private final NumberSetting range = register(new NumberSetting("Range", 50, 10, 100, 5));

    public Radar() {
        super("Radar", "Mini radar gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onRender2D(EventRender2D e) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        int s = size.getInt();
        int x = 10, y = 30;
        RenderUtils.drawBorderedRect(x, y, s, s, 0x88000000, 1, 0xFFFFFFFF);
        RenderUtils.draw2DRect(x + s / 2 - 1, y + s / 2 - 1, 2, 2, 0xFF00FF00);

        double maxRange = range.getValue();
        for (EntityPlayer p : mc.theWorld.playerEntities) {
            if (p == mc.thePlayer) continue;
            double dx = p.posX - mc.thePlayer.posX;
            double dz = p.posZ - mc.thePlayer.posZ;
            if (Math.abs(dx) > maxRange || Math.abs(dz) > maxRange) continue;
            int rx = x + s / 2 + (int) (dx / maxRange * (s / 2 - 3));
            int ry = y + s / 2 + (int) (dz / maxRange * (s / 2 - 3));
            RenderUtils.draw2DRect(rx, ry, 3, 3, 0xFFFF5555);
        }
    }
}
