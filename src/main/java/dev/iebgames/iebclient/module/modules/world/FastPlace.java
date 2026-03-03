package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class FastPlace extends Module {

    public FastPlace() {
        super("FastPlace", "Blok koyma hızını arttırır.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        mc.rightClickDelayTimer = 0;
    }

    @Override
    protected void onDisable() {
        mc.rightClickDelayTimer = 6;
    }
}
