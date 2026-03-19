package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventMotion;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.BooleanSetting;
import dev.iebgames.iebclient.setting.ModeSetting;
import dev.iebgames.iebclient.setting.NumberSetting;
import dev.iebgames.iebclient.util.PlayerUtils;
import dev.iebgames.iebclient.util.TimerUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

public class KillAura extends Module {

    private final NumberSetting  range     = register(new NumberSetting("Range", 4.0, 3.0, 6.0, 0.1));
    private final NumberSetting  cps       = register(new NumberSetting("CPS", 12, 1, 20, 1));
    private final ModeSetting    sortMode  = register(new ModeSetting("Sort", "Distance", "Health", "Armor"));
    private final BooleanSetting players   = register(new BooleanSetting("Players", true));
    private final BooleanSetting mobs      = register(new BooleanSetting("Mobs", true));
    private final BooleanSetting rotate    = register(new BooleanSetting("Rotate", true));
    private final BooleanSetting criticals = register(new BooleanSetting("Criticals", false));

    private final TimerUtils timer = new TimerUtils();
    private EntityLivingBase target;
    private float[] rotations;

    public KillAura() {
        super("KillAura", "Otomatik en yakın hedefe saldırı.", Category.COMBAT, Keyboard.KEY_K);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        target = PlayerUtils.getClosestTarget(range.getValue(), players.isEnabled(), mobs.isEnabled());
        if (target == null) return;

        if (rotate.isEnabled()) {
            rotations = PlayerUtils.getRotationsToEntity(target);
        }

        long delay = (long)(1000.0 / cps.getInt());
        if (timer.hasReached(delay)) {
            if (criticals.isEnabled() && mc.thePlayer.onGround) {
                mc.thePlayer.jump();
            }
            mc.playerController.attackEntity(mc.thePlayer, target);
            mc.thePlayer.swingItem();
            timer.reset();
        }
    }

    @EventHook
    public void onMotion(EventMotion e) {
        if (!e.isPre() || rotations == null || target == null) return;
        e.setYaw(rotations[0]);
        e.setPitch(rotations[1]);
    }

    @Override
    protected void onDisable() {
        target = null;
        rotations = null;
    }

    public EntityLivingBase getTarget() {
        return target;
    }
}
