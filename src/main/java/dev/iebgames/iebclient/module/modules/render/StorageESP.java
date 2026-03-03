package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class StorageESP extends Module {

    public StorageESP() {
        super("StorageESP", "Depolama bloklarını gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
