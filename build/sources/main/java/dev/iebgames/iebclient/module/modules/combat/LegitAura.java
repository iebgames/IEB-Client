package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventMotion;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.BooleanSetting;
import dev.iebgames.iebclient.setting.NumberSetting;
import dev.iebgames.iebclient.util.PlayerUtils;
import dev.iebgames.iebclient.util.RotationUtils;
import dev.iebgames.iebclient.util.TimerUtils;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

import java.util.Random;

public class LegitAura extends Module {

    private final NumberSetting range = register(new NumberSetting("Range", 3.2, 2.5, 4.5, 0.1));
    private final NumberSetting minCps = register(new NumberSetting("Min CPS", 6, 1, 15, 1));
    private final NumberSetting maxCps = register(new NumberSetting("Max CPS", 10, 1, 20, 1));
    private final BooleanSetting players = register(new BooleanSetting("Players", true));
    private final BooleanSetting mobs = register(new BooleanSetting("Mobs", false));

    private final TimerUtils timer = new TimerUtils();
    private final Random random = new Random();
    private long nextDelay = 100;

    public LegitAura() {
        super("LegitAura", "Humanize edilmiş otomatik saldırı.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        EntityLivingBase target = PlayerUtils.getClosestTarget(range.getValue(), players.isEnabled(), mobs.isEnabled());
        if (target == null) return;
        if (!timer.hasReached(nextDelay)) return;

        mc.playerController.attackEntity(mc.thePlayer, target);
        mc.thePlayer.swingItem();

        int min = minCps.getInt();
        int max = Math.max(min, maxCps.getInt());
        nextDelay = (long) (1000.0 / (min + random.nextInt(max - min + 1)));
        timer.reset();
    }

    @EventHook
    public void onMotion(EventMotion e) {
        if (!e.isPre()) return;
        EntityLivingBase target = PlayerUtils.getClosestTarget(range.getValue(), players.isEnabled(), mobs.isEnabled());
        if (target == null) return;
        float[] rot = PlayerUtils.getRotationsToEntity(target);
        e.setYaw(RotationUtils.updateRotation(mc.thePlayer.rotationYaw, rot[0], 25 + random.nextInt(15)));
        e.setPitch(RotationUtils.updateRotation(mc.thePlayer.rotationPitch, rot[1], 20 + random.nextInt(10)));
    }
}
