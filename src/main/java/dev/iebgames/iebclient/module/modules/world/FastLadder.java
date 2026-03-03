package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class FastLadder extends Module {
    public FastLadder() {
        super("FastLadder", "Merdivenleri daha hızlı tırmanırsın.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.isOnLadder()) {
            mc.thePlayer.motionY = 0.5;
        }
    }
}
