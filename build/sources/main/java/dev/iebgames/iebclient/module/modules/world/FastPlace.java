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
        ((dev.iebgames.iebclient.mixin.MinecraftAccessor)mc).setRightClickDelayTimer(0);
    }

    @Override
    public void onDisable() {
        ((dev.iebgames.iebclient.mixin.MinecraftAccessor)mc).setRightClickDelayTimer(6);
    }
}
