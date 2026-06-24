package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventRender3D;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.ColorSetting;
import dev.iebgames.iebclient.util.EspRenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public class TrueSight extends Module {

    private final ColorSetting color = register(new ColorSetting("Color", 0xB4FF00FF));

    public TrueSight() {
        super("TrueSight", "Görünmez oyuncuları gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onRender3D(EventRender3D e) {
        if (mc.theWorld == null) return;
        for (EntityPlayer p : mc.theWorld.playerEntities) {
            if (p != mc.thePlayer && p.isInvisible()) {
                EspRenderHelper.drawEntityBox(p, color.getColor(), e.getPartialTicks());
            }
        }
    }
}
