package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class ItemESP extends Module {

    public ItemESP() {
        super("ItemESP", "Yerdeki eşyaları gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
