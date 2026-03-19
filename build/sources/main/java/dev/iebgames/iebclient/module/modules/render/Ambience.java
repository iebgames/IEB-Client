package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Ambience extends Module {

    private final NumberSetting time = register(new NumberSetting("Time", 18000, 0, 24000, 1000));

    public Ambience() {
        super("Ambience", "Dünya zamanını (gündüz/gece) değiştirir.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        mc.theWorld.setWorldTime(time.getLong());
    }
}
