package dev.iebgames.iebclient.setting;

public class ModeSetting extends Setting<String> {
    private final String[] modes;
    private int index;

    public ModeSetting(String name, String... modes) {
        super(name, modes[0]);
        this.modes = modes;
        this.index = 0;
    }

    public String[] getModes()  { return modes; }
    public int      getIndex()  { return index; }

    public void next() {
        index = (index + 1) % modes.length;
        value = modes[index];
    }

    public void setMode(String m) {
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].equalsIgnoreCase(m)) { index = i; value = modes[i]; return; }
        }
    }

    public boolean is(String m) { return value.equalsIgnoreCase(m); }
    public String  getMode()   { return value; }
}
