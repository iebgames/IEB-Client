package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Animations extends Module {

    private final NumberSetting speed = register(new NumberSetting("Swing Speed", 1.0, 0.5, 2.0, 0.1));

    public Animations() {
        super("Animations", "Vurma/bloklama animasyon hızını değiştirir.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer != null) {
            mc.thePlayer.swingProgressInt = (int) (mc.thePlayer.swingProgress * speed.getFloat());
        }
    }
}
