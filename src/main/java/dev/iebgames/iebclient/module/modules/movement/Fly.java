package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Fly extends Module {

    private final NumberSetting speed = register(new NumberSetting("Speed", 1.0, 0.1, 5.0, 0.1));

    public Fly() {
        super("Fly", "Havada uçmanı sağlar.", Category.MOVEMENT, Keyboard.KEY_F);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.jumpMovementFactor = speed.getFloat();

        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.thePlayer.motionY += speed.getValue();
        }
        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.thePlayer.motionY -= speed.getValue();
        }
    }

    @Override
    protected void onDisable() {
        mc.thePlayer.capabilities.isFlying = false;
    }
}
