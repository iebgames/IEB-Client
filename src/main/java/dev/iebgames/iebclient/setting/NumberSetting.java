package dev.iebgames.iebclient.setting;

public class NumberSetting extends Setting<Double> {
    private final double min, max, step;

    public NumberSetting(String name, double def, double min, double max, double step) {
        super(name, def);
        this.min  = min;
        this.max  = max;
        this.step = step;
    }

    public double getMin()  { return min; }
    public double getMax()  { return max; }
    public double getStep() { return step; }

    @Override
    public void setValue(Double v) {
        this.value = Math.max(min, Math.min(max, v));
    }

    public float  getFloat() { return value.floatValue(); }
    public int    getInt()   { return value.intValue(); }
}
