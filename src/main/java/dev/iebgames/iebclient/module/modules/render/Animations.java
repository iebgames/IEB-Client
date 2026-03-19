package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.ModeSetting;
import org.lwjgl.input.Keyboard;

public class Animations extends Module {

    private final ModeSetting mode = register(new ModeSetting("Mode", "1.7", "Slide", "Sigma", "Swing"));

    public Animations() {
        super("Animations", "Bloklama ve vurma animasyonlarını değiştirir.", Category.RENDER, Keyboard.KEY_NONE);
    }

    public String getAnimationMode() {
        return mode.getMode();
    }
}
