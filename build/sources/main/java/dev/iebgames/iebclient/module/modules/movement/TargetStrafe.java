package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.module.modules.combat.KillAura;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

public class TargetStrafe extends Module {

    private final NumberSetting range = register(new NumberSetting("Range", 3.0, 1.0, 6.0, 0.1));
    private int direction = 1;

    public TargetStrafe() {
        super("TargetStrafe", "Hedefin etrafında daire çizer.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        KillAura aura = IEBClient.INSTANCE.moduleManager.getModule(KillAura.class);
        if (aura == null || !aura.isEnabled()) return;

        EntityLivingBase target = aura.getTarget();
        if (target == null) return;

        if (mc.thePlayer.isCollidedHorizontally) direction *= -1;

        if (mc.thePlayer.getDistanceToEntity(target) > range.getValue()) {
            mc.thePlayer.moveForward = 1;
        } else {
            mc.thePlayer.moveForward = 0;
            mc.thePlayer.moveStrafing = direction;
        }
    }
}
