package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class NameProtect extends Module {

    public NameProtect() {
        super("NameProtect", "İsmini başkalarına karşı gizler.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
