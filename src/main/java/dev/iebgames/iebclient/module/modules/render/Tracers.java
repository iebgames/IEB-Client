package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventRender3D;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.BooleanSetting;
import dev.iebgames.iebclient.setting.ColorSetting;
import dev.iebgames.iebclient.util.EspRenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public class Tracers extends Module {

    private final ColorSetting color = register(new ColorSetting("Color", 0xC8FFFFFF));
    private final BooleanSetting players = register(new BooleanSetting("Players", true));

    public Tracers() {
        super("Tracers", "Kişilere doğru çizgiler çizer.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onRender3D(EventRender3D e) {
        if (!players.isEnabled() || mc.theWorld == null) return;
        for (EntityPlayer p : mc.theWorld.playerEntities) {
            if (p != mc.thePlayer) EspRenderHelper.drawTracer(p, color.getColor(), e.getPartialTicks());
        }
    }
}
