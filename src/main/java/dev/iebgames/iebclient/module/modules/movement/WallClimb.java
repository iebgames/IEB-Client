package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class WallClimb extends Module {

    private final NumberSetting speed = register(new NumberSetting("Speed", 0.2, 0.1, 1.0, 0.05));

    public WallClimb() {
        super("WallClimb", "Dikey duvarlara tırmanmanı sağlar.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.isCollidedHorizontally) {
            mc.thePlayer.motionY = speed.getValue();
        }
    }
}
