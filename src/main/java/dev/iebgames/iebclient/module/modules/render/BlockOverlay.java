package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class BlockOverlay extends Module {

    public BlockOverlay() {
        super("BlockOverlay", "Baktığın bloğun etrafını vurgular.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
