package dev.iebgames.iebclient.module;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.setting.Setting;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {

    protected static final Minecraft mc = Minecraft.getMinecraft();

    private final String   name;
    private final String   description;
    private final Category category;
    private       int      keybind;
    private       boolean  enabled;
    private       boolean  registered;

    protected final List<Setting<?>> settings = new ArrayList<>();

    protected final int defaultKeybind;
    protected final boolean defaultEnabled;

    public Module(String name, String description, Category category, int keybind) {
        this.name        = name;
        this.description = description;
        this.category    = category;
        this.keybind     = keybind;
        this.defaultKeybind = keybind;
        this.defaultEnabled = false; // By default everything is off except ClickGUI but we'll manage it
    }

    public void reset() {
        if (this.enabled != this.defaultEnabled) {
            setEnabled(this.defaultEnabled);
        }
        setKeybind(this.defaultKeybind);
        for (Setting<?> s : settings) {
            s.reset();
        }
    }

    // ── Lifecycle ──────────────────────────────────────────────────
    public void toggle() {
        setEnabled(!enabled);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            if (!registered) {
                IEBClient.eventBus.subscribe(this);
                registered = true;
            }
            onEnable();
        } else {
            if (registered) {
                IEBClient.eventBus.unsubscribe(this);
                registered = false;
            }
            onDisable();
        }
    }

    protected void onEnable()  {}
    protected void onDisable() {}

    // ── Settings helpers ───────────────────────────────────────────
    protected <T extends Setting<?>> T register(T setting) {
        settings.add(setting);
        return setting;
    }

    // ── Getters ────────────────────────────────────────────────────
    public String         getName()        { return name; }
    public String         getDescription() { return description; }
    public Category       getCategory()    { return category; }
    public int            getKeybind()     { return keybind; }
    public void           setKeybind(int k){ this.keybind = k; }
    public boolean        isEnabled()      { return enabled; }
    public boolean        isToggled()      { return enabled; }
    public List<Setting<?>> getSettings()  { return settings; }

    @Override public String toString()     { return name; }
}
