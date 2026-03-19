package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class HighJump extends Module {

    private final NumberSetting height = register(new NumberSetting("Height", 1.0, 0.5, 5.0, 0.5));

    public HighJump() {
        super("HighJump", "Yükseğe zıplamanı sağlar.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.gameSettings.keyBindJump.isPressed() && mc.thePlayer.onGround) {
            mc.thePlayer.motionY = height.getValue();
        }
    }
}
