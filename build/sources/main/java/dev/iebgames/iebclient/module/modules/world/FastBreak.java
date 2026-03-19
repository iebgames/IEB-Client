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
        dev.iebgames.iebclient.mixin.PlayerControllerAccessor pca = (dev.iebgames.iebclient.mixin.PlayerControllerAccessor)mc.playerController;
        if (pca.getCurBlockDamageMP() > 0.7f) {
            pca.setCurBlockDamageMP(1.0f);
        }
    }
}
