package dev.iebgames.iebclient.util;

public final class MathUtils {
    public static double lerp(double a, double b, double t)         { return a + (b - a) * t; }
    public static float  clamp(float v, float min, float max)       { return Math.max(min, Math.min(max, v)); }
    public static double clampD(double v, double min, double max)   { return Math.max(min, Math.min(max, v)); }
    public static float  normalizeAngle(float angle) {
        while (angle > 180f)  angle -= 360f;
        while (angle < -180f) angle += 360f;
        return angle;
    }
    public static int randomRange(int min, int max) {
        return min + (int)(Math.random() * (max - min + 1));
    }
}
