package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.BooleanSetting;
import dev.iebgames.iebclient.setting.ColorSetting;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Crosshair extends Module {

    public final NumberSetting size = register(new NumberSetting("Size", 5.0, 1.0, 20.0, 0.5));
    public final NumberSetting gap = register(new NumberSetting("Gap", 2.0, 0.0, 10.0, 0.5));
    public final ColorSetting color = register(new ColorSetting("Color", 0xFF00FF00));
    public final BooleanSetting chroma = register(new BooleanSetting("Chroma", false));

    public Crosshair() {
        super("Crosshair", "Ekrandaki nişangahı özelleştirmeni sağlar.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
