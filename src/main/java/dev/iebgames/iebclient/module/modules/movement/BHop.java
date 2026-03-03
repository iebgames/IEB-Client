package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class BHop extends Module {

    public BHop() {
        super("BHop", "Otomatik zıplayarak hızlanır.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
            }
        }
    }
}
