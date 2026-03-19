package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class MobESP extends Module {

    public MobESP() {
        super("MobESP", "Mobların yerini gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
