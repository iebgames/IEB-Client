package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class DamageIndicator extends Module {

    public DamageIndicator() {
        super("DamageIndicator", "Verdiğin hasarı gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
