package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class NoHurtCam extends Module {

    public NoHurtCam() {
        super("NoHurtCam", "Hasar aldığında ekranın sallanmasını engeller.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
