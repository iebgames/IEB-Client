package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class TrueSight extends Module {

    public TrueSight() {
        super("TrueSight", "Görünmez olan oyuncuları ve bariyerleri görmeni sağlar.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
