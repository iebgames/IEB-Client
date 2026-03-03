package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class ReverseStep extends Module {

    public ReverseStep() {
        super("ReverseStep", "Hızlıca aşağı düşmeni sağlar.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (!mc.thePlayer.onGround && mc.thePlayer.motionY < 0 && !mc.thePlayer.isInWater()) {
            mc.thePlayer.motionY = -1.0;
        }
    }
}
