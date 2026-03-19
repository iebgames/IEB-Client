package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class FastClimb extends Module {

    private final NumberSetting speed = register(new NumberSetting("Speed", 0.3, 0.1, 1.0, 0.05));

    public FastClimb() {
        super("FastClimb", "Merdivenleri ve sarmaşıkları hızla tırmanmanı sağlar.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.isOnLadder()) {
            mc.thePlayer.motionY = speed.getValue();
        }
    }
}
