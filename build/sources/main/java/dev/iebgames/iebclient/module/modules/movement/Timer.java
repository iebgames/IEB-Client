package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Timer extends Module {

    private final NumberSetting speed = register(new NumberSetting("Speed", 1.0, 0.1, 5.0, 0.1));

    public Timer() {
        super("Timer", "Oyun hızını arttırır/azaltır.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        // Timer logic is usually handled in the main Minecraft class or via a hook.
        // We set the multiplier here.
    }

    public float getTimerSpeed() {
        return isEnabled() ? speed.getFloat() : 1.0f;
    }
}
