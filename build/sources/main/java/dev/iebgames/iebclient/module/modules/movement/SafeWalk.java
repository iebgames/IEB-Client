package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class SafeWalk extends Module {

    public SafeWalk() {
        super("SafeWalk", "Blok kenarlarından düşmeni engeller.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        // This is usually handled in MixinEntityPlayerSP or similar, 
        // as a flag or logic override. We'll add the property here.
    }
}
