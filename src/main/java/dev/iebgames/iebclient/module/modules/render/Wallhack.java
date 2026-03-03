package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class Wallhack extends Module {

    public Wallhack() {
        super("Wallhack", "Entityleri blokların arkasından görmeni sağlar.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
