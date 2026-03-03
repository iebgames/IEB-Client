package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {

    private final NumberSetting speed = register(new NumberSetting("Speed", 1.2, 1.0, 5.0, 0.1));

    public Speed() {
        super("Speed", "Yürüme hızını arttırır.", Category.MOVEMENT, Keyboard.KEY_V);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.motionX *= speed.getValue();
                mc.thePlayer.motionZ *= speed.getValue();
            }
        }
    }
}
