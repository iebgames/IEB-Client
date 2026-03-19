package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class LiquidInteract extends Module {
    public LiquidInteract() {
        super("LiquidInteract", "Sıvılara (su/lav) blok koymanı sağlar.", Category.MISC, Keyboard.KEY_NONE);
    }
    // Logic handled in MixinBlock (canClickBlock or similar)
}
