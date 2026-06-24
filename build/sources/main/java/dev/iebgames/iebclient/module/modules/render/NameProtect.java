package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.TextSetting;
import org.lwjgl.input.Keyboard;

public class NameProtect extends Module {

    public static String fakeName = "Player";
    private final TextSetting name = register(new TextSetting("Name", "Player"));

    public NameProtect() {
        super("NameProtect", "İsmini gizler/değiştirir.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @Override
    protected void onEnable() {
        fakeName = name.getValue();
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        fakeName = name.getValue();
    }
}
