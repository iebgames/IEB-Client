package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class Chams extends Module {

    public Chams() {
        super("Chams", "Oyuncuları duvar arkasından renkli görmeni sağlar.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
