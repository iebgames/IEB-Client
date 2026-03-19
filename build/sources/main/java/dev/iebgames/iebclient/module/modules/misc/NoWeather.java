package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class NoWeather extends Module {
    public NoWeather() {
        super("NoWeather", "Hava durumunu (yağmur/kar) kapatır.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.theWorld != null) {
            mc.theWorld.setRainStrength(0);
            mc.theWorld.setThunderStrength(0);
        }
    }
}
