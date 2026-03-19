package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventMotion;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import dev.iebgames.iebclient.util.MathUtils;
import dev.iebgames.iebclient.util.PlayerUtils;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

public class AimAssist extends Module {

    private final NumberSetting range = register(new NumberSetting("Range", 5.0, 2.0, 8.0, 0.5));
    private final NumberSetting speed = register(new NumberSetting("Speed", 3.0, 0.5, 10.0, 0.5));
    private final NumberSetting fov   = register(new NumberSetting("FOV", 90, 10, 180, 5));

    public AimAssist() {
        super("AimAssist", "Yakın hedefe yavaşça nişan yardımı.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onMotion(EventMotion e) {
        if (!e.isPre()) return;
        EntityLivingBase target = PlayerUtils.getClosestTarget(range.getValue(), true, true);
        if (target == null) return;
        float[] needed = PlayerUtils.getRotationsToEntity(target);
        float yaw   = e.getYaw();
        float pitch = e.getPitch();
        float dYaw   = MathUtils.normalizeAngle(needed[0] - yaw);
        float dPitch = needed[1] - pitch;
        if (Math.abs(dYaw) > fov.getFloat() / 2f) return;
        float t = (float)(speed.getValue() * 0.05f);
        e.setYaw(yaw   + dYaw   * t);
        e.setPitch(pitch + dPitch * t);
    }
}
