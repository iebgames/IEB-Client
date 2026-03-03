package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventMotion;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.util.PlayerUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import org.lwjgl.input.Keyboard;

public class BowAimbot extends Module {

    public BowAimbot() {
        super("BowAimbot", "Ok atarken otomatik nişan alır.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onMotion(EventMotion e) {
        if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBow && mc.thePlayer.isUsingItem()) {
            EntityLivingBase target = PlayerUtils.getClosestTarget(40.0, true, true);
            if (target != null) {
                float[] rotations = PlayerUtils.getRotationsToEntity(target);
                // Basic gravity compensation could be added here
                e.setYaw(rotations[0]);
                e.setPitch(rotations[1]);
            }
        }
    }
}
