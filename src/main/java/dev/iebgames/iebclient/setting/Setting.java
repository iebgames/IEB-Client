package dev.iebgames.iebclient.setting;

public abstract class Setting<T> {
    protected final String name;
    protected T value;
    private   String description;

    public Setting(String name, T defaultValue) {
        this.name  = name;
        this.value = defaultValue;
    }

    public Setting<T> desc(String d) { this.description = d; return this; }
    public String     getName()      { return name; }
    public String     getDesc()      { return description != null ? description : ""; }
    public T          getValue()     { return value; }
    public void       setValue(T v)  { this.value = v; }
}
