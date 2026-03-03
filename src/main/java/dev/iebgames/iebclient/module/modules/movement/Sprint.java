package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", "Otomatik sprint atmanı sağlar.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.moveForward > 0 && !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isSneaking()) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
