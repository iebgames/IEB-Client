package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class ReplayRecorder extends Module {
    public ReplayRecorder() {
        super("ReplayRecorder", "Oyununu kaydeder (Placeholder).", Category.MISC, Keyboard.KEY_NONE);
    }
}
