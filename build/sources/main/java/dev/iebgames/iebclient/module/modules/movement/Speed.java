package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.ModeSetting;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {

    private final ModeSetting mode = register(new ModeSetting("Mode", "Motion", "Hypixel", "OnGround"));
    private final NumberSetting speed = register(new NumberSetting("Speed", 1.2, 1.0, 5.0, 0.1));

    public Speed() {
        super("Speed", "Yürüme hızını arttırır (Bypass modları desteklenir).", Category.MOVEMENT, Keyboard.KEY_V);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
            switch (mode.getMode()) {
                case "Motion":
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionX *= speed.getValue();
                        mc.thePlayer.motionZ *= speed.getValue();
                    }
                    break;
                case "Hypixel":
                    // Simplified Hypixel spoof: jump and boost
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        mc.thePlayer.motionX *= 1.02;
                        mc.thePlayer.motionZ *= 1.02;
                    }
                    break;
                case "OnGround":
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionX *= (speed.getValue() * 1.5);
                        mc.thePlayer.motionZ *= (speed.getValue() * 1.5);
                    }
                    break;
            }
        }
    }
}
