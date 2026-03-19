package dev.iebgames.iebclient.util;

import java.awt.Color;

public class ColorUtils {

    public static int getChroma(long speed, long delay) {
        float hue = (System.currentTimeMillis() + delay) % speed;
        hue /= (float) speed;
        return Color.getHSBColor(hue, 1f, 1f).getRGB();
    }

    public static int applyAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    public static int getColor(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) |
               ((r & 0xFF) << 16) |
               ((g & 0xFF) << 8)  |
               ((b & 0xFF) << 0);
    }
}
