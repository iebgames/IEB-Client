package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class Tracers extends Module {

    public Tracers() {
        super("Tracers", "Kişilere doğru çizgiler çizer.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
