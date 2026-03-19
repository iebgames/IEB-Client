package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Step extends Module {

    private final NumberSetting height = register(new NumberSetting("Height", 1.0, 0.5, 2.5, 0.5));

    public Step() {
        super("Step", "Blokları otomatik çıkar.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        mc.thePlayer.stepHeight = height.getFloat();
    }

    @Override
    protected void onDisable() {
        mc.thePlayer.stepHeight = 0.6f;
    }
}
