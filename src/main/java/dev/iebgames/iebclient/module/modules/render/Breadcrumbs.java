package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class Breadcrumbs extends Module {

    public Breadcrumbs() {
        super("Breadcrumbs", "Geçtiğin yerlerde iz bırakır.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
