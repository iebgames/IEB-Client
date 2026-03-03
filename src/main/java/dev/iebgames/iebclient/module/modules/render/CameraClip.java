package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class CameraClip extends Module {

    public CameraClip() {
        super("CameraClip", "Kameranın blokların içinden geçmesini sağlar.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
