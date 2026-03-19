package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class ChestESP extends Module {

    public ChestESP() {
        super("ChestESP", "Sandıkların yerini gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
