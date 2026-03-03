package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class Fullbright extends Module {

    public Fullbright() {
        super("Fullbright", "Dünyayı en yüksek parlaklıkta görmeni sağlar.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        mc.gameSettings.gammaSetting = 100f;
    }

    @Override
    protected void onDisable() {
        mc.gameSettings.gammaSetting = 1f;
    }
}
