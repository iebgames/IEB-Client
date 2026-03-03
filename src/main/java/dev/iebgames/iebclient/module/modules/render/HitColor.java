package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class HitColor extends Module {

    public HitColor() {
        super("HitColor", "Hasar alan kişilerin rengini değiştirir.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
