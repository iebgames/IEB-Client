package dev.iebgames.iebclient.setting;

import dev.iebgames.iebclient.util.ColorUtils;
import java.awt.Color;

public class ColorSetting extends Setting<Integer> {
    
    private boolean chroma;
    private int red, green, blue, alpha;

    public ColorSetting(String name, int defaultValue) {
        super(name, defaultValue);
        updateComponents();
    }

    private void updateComponents() {
        Color c = new Color(value, true);
        this.red = c.getRed();
        this.green = c.getGreen();
        this.blue = c.getBlue();
        this.alpha = c.getAlpha();
    }

    public boolean isChroma() { return chroma; }
    public void setChroma(boolean chroma) { this.chroma = chroma; }

    public int getRed() { return red; }
    public void setRed(int red) { this.red = red; updateValue(); }

    public int getGreen() { return green; }
    public void setGreen(int green) { this.green = green; updateValue(); }

    public int getBlue() { return blue; }
    public void setBlue(int blue) { this.blue = blue; updateValue(); }

    public int getAlpha() { return alpha; }
    public void setAlpha(int alpha) { this.alpha = alpha; updateValue(); }

    private void updateValue() {
        this.value = new Color(red, green, blue, alpha).getRGB();
    }

    public int getColor() {
        if (chroma) return ColorUtils.applyAlpha(ColorUtils.getChroma(3000, 0), alpha);
        return value;
    }
}
