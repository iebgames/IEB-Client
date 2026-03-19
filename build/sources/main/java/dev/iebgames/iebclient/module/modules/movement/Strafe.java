package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class Strafe extends Module {

    public Strafe() {
        super("Strafe", "Havada hareket kontrolünü arttırır.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (!mc.thePlayer.onGround) {
            // Simplified strafe logic
            double speed = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
            double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
            if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
                mc.thePlayer.motionX = -Math.sin(yaw) * speed;
                mc.thePlayer.motionZ = Math.cos(yaw) * speed;
            }
        }
    }
}
