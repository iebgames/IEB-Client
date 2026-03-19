package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class AutoQueue extends Module {
    public AutoQueue() {
        super("AutoQueue", "Sıradaki oyuna otomatik girer.", Category.MISC, Keyboard.KEY_NONE);
    }
    // Scans chat for play-again commands
}
