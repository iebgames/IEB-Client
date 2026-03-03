package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class AntiOverlay extends Module {

    public AntiOverlay() {
        super("AntiOverlay", "Su, ateş, ve balkabağı gibi görüntüleri engeller.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
