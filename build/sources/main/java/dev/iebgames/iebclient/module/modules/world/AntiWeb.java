package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class AntiWeb extends Module {
    public AntiWeb() {
        super("AntiWeb", "Örümcek ağlarında yavaşlamazsın.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        dev.iebgames.iebclient.mixin.EntityAccessor ea = (dev.iebgames.iebclient.mixin.EntityAccessor)mc.thePlayer;
        if (ea.getIsInWeb()) {
            ea.setIsInWeb(false);
        }
    }
}
