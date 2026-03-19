package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class Jesus extends Module {

    public Jesus() {
        super("Jesus", "Su üstünde yürümeni sağlar.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.isInWater() && !mc.thePlayer.isSneaking()) {
            mc.thePlayer.motionY = 0.1;
        }
    }
}
