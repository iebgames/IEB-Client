package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.BooleanSetting;
import dev.iebgames.iebclient.setting.ColorSetting;
import org.lwjgl.input.Keyboard;

public class BlockOverlay extends Module {

    public final ColorSetting color = register(new ColorSetting("Color", 0x88FF0000));
    public final BooleanSetting chroma = register(new BooleanSetting("Chroma", false));

    public BlockOverlay() {
        super("BlockOverlay", "Highlights the block you are looking at.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
