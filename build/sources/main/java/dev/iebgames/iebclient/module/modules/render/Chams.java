package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.ColorSetting;
import org.lwjgl.input.Keyboard;

public class Chams extends Module {

    private final ColorSetting color = register(new ColorSetting("Color", 0x8CFF3C3C));

    public Chams() {
        super("Chams", "Duvar arkası renkli oyuncu gösterimi.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
