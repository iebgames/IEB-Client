package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class PlayerESP extends Module {

    public PlayerESP() {
        super("PlayerESP", "Oyuncuların etrafına kutu çizer.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
