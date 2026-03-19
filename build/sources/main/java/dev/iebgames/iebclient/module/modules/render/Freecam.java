package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

public class Freecam extends Module {

    private double oldX, oldY, oldZ;
    private float oldYaw, oldPitch;

    public Freecam() {
        super("Freecam", "Gövdeni bırakıp kamerayla özgürce gezmeni sağlar.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @Override
    protected void onEnable() {
        oldX = mc.thePlayer.posX;
        oldY = mc.thePlayer.posY;
        oldZ = mc.thePlayer.posZ;
        oldYaw = mc.thePlayer.rotationYaw;
        oldPitch = mc.thePlayer.rotationPitch;
        mc.thePlayer.noClip = true;
    }

    @Override
    protected void onDisable() {
        mc.thePlayer.setPosition(oldX, oldY, oldZ);
        mc.thePlayer.rotationYaw = oldYaw;
        mc.thePlayer.rotationPitch = oldPitch;
        mc.thePlayer.noClip = false;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        mc.thePlayer.noClip = true;
        mc.thePlayer.onGround = false;
        mc.thePlayer.motionY = 0;
        if (mc.gameSettings.keyBindJump.isKeyDown()) mc.thePlayer.motionY = 0.5;
        if (mc.gameSettings.keyBindSneak.isKeyDown()) mc.thePlayer.motionY = -0.5;
    }
}
