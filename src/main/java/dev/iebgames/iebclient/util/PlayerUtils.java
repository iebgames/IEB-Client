package dev.iebgames.iebclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class PlayerUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static List<EntityLivingBase> getTargets(double range, boolean players, boolean mobs) {
        List<EntityLivingBase> list = new ArrayList<>();
        for (Entity e : mc.theWorld.loadedEntityList) {
            if (!(e instanceof EntityLivingBase)) continue;
            if (e == mc.thePlayer) continue;
            EntityLivingBase living = (EntityLivingBase) e;
            if (living.isDead || living.getHealth() <= 0) continue;
            if (!players && e instanceof EntityPlayer) continue;
            if (!mobs && !(e instanceof EntityPlayer)) continue;
            if (mc.thePlayer.getDistanceSqToEntity(e) <= range * range) list.add(living);
        }
        return list;
    }

    public static EntityLivingBase getClosestTarget(double range, boolean players, boolean mobs) {
        return getTargets(range, players, mobs).stream()
            .min(Comparator.comparingDouble(e -> mc.thePlayer.getDistanceSqToEntity(e)))
            .orElse(null);
    }

    public static float[] getRotationsToEntity(Entity entity) {
        double dx = entity.posX - mc.thePlayer.posX;
        double dy = (entity.posY + entity.getEyeHeight() / 2.0) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double dz = entity.posZ - mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(dx * dx + dz * dz);
        float yaw   = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        float pitch = (float)(-Math.toDegrees(Math.atan2(dy, dist)));
        return new float[]{yaw, pitch};
    }
}
