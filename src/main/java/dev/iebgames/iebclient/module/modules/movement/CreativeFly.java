package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class CreativeFly extends Module {

    public CreativeFly() {
        super("CreativeFly", "Creative uçuşunu aktifleştirir.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        mc.thePlayer.capabilities.isFlying = true;
    }

    @Override
    protected void onDisable() {
        if (!mc.playerController.isInCreativeMode()) {
            mc.thePlayer.capabilities.isFlying = false;
        }
    }
}
