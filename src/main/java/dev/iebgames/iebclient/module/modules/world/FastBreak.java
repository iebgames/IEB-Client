package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class FastBreak extends Module {

    public FastBreak() {
        super("FastBreak", "Blokları daha hızlı kırmanı sağlar.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.playerController.curBlockDamageMP > 0.7f) {
            mc.playerController.curBlockDamageMP = 1.0f;
        }
    }
}
