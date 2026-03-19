package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Hitbox extends Module {

    private final NumberSetting size = register(new NumberSetting("Size", 0.15, 0.01, 0.5, 0.01));

    public Hitbox() {
        super("Hitbox", "Entity hitbox'ını genişletir.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    public double getExpansion() { return size.getValue(); }
}
