package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class Macro extends Module {
    public Macro() {
        super("Macro", "Tuşlara komut atamanı sağlar.", Category.MISC, Keyboard.KEY_NONE);
    }
}
