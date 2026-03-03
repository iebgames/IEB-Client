package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import dev.iebgames.iebclient.util.TimerUtils;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

public class Triggerbot extends Module {

    private final NumberSetting delay = register(new NumberSetting("Delay (ms)", 80, 10, 500, 10));

    private final TimerUtils timer = new TimerUtils();

    public Triggerbot() {
        super("Triggerbot", "Crosshair'daki hedefe otomatik tıklar.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.objectMouseOver == null) return;
        if (!(mc.objectMouseOver.entityHit instanceof EntityLivingBase)) return;
        EntityLivingBase target = (EntityLivingBase) mc.objectMouseOver.entityHit;
        if (target == mc.thePlayer) return;
        if (!timer.hasReached((long) delay.getValue())) return;
        mc.playerController.attackEntity(mc.thePlayer, target);
        mc.thePlayer.swingItem();
        timer.reset();
    }
}
