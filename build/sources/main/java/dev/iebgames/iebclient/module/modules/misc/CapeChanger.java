package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class CapeChanger extends Module {
    public CapeChanger() {
        super("CapeChanger", "Pelerinini değiştirmene yarar (Client-side).", Category.MISC, Keyboard.KEY_NONE);
    }
}
