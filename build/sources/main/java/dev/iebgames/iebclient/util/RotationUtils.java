package dev.iebgames.iebclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public final class RotationUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float[] getRotations(BlockPos pos, EnumFacing facing) {
        double x = pos.getX() + 0.5 + facing.getFrontOffsetX() * 0.5 - mc.thePlayer.posX;
        double y = pos.getY() + 0.5 + facing.getFrontOffsetY() * 0.5 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double z = pos.getZ() + 0.5 + facing.getFrontOffsetZ() * 0.5 - mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float) -(Math.atan2(y, dist) * 180.0 / Math.PI);
        return new float[]{yaw, pitch};
    }

    public static float updateRotation(float current, float target, float maxChange) {
        float diff = MathHelper.wrapAngleTo180_float(target - current);
        if (diff > maxChange) diff = maxChange;
        if (diff < -maxChange) diff = -maxChange;
        return current + diff;
    }
}
