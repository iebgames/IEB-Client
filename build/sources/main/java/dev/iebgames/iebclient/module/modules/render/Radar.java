package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class Radar extends Module {

    public Radar() {
        super("Radar", "Ekranda bir radarla rakipleri gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
