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

    @Override
    protected void onEnable() {
        mc.thePlayer.capabilities.allowFlying = true;
    }

    @Override
    protected void onDisable() {
        if (!mc.playerController.isInCreativeMode()) {
            mc.thePlayer.capabilities.allowFlying = false;
            mc.thePlayer.capabilities.isFlying = false;
        }
        mc.thePlayer.motionY = 0;
    }
}
