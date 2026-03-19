package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class KeepSprint extends Module {

    public KeepSprint() {
        super("KeepSprint", "Saldırırken yavaşlamanı engeller.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        // In some client versions, mc.thePlayer.isSprinting() = false on attack.
        // We force it to true if we are moving.
        if (mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSprinting() && !mc.thePlayer.isCollidedHorizontally) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
