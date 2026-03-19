package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.ModeSetting;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Fly extends Module {

    private final ModeSetting mode = register(new ModeSetting("Mode", "Normal", "Motion", "Vanilla"));
    private final NumberSetting speed = register(new NumberSetting("Speed", 1.0, 0.1, 5.0, 0.1));

    public Fly() {
        super("Fly", "Havada uçmanı sağlar (FDP Bypass modları dahil).", Category.MOVEMENT, Keyboard.KEY_F);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        switch (mode.getMode()) {
            case "Vanilla":
                mc.thePlayer.capabilities.isFlying = true;
                break;
            case "Motion":
                mc.thePlayer.motionY = 0;
                if (mc.gameSettings.keyBindJump.isKeyDown()) mc.thePlayer.motionY = speed.getValue();
                if (mc.gameSettings.keyBindSneak.isKeyDown()) mc.thePlayer.motionY = -speed.getValue();
                break;
            case "Normal":
                mc.thePlayer.capabilities.isFlying = false;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.jumpMovementFactor = speed.getFloat();
                if (mc.gameSettings.keyBindJump.isKeyDown()) mc.thePlayer.motionY += speed.getValue();
                if (mc.gameSettings.keyBindSneak.isKeyDown()) mc.thePlayer.motionY -= speed.getValue();
                break;
        }
    }

    @Override
    protected void onDisable() {
        mc.thePlayer.capabilities.isFlying = false;
        if (!mc.playerController.isInCreativeMode()) {
            mc.thePlayer.capabilities.allowFlying = false;
        }
        mc.thePlayer.jumpMovementFactor = 0.02F;
    }
}
