package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class ChatBypass extends Module {
    public ChatBypass() {
        super("ChatBypass", "Sohbet filtrelerini aşmaya çalışır.", Category.MISC, Keyboard.KEY_NONE);
    }
    // Often works by adding random characters between letters
}
