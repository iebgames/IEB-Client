package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class BlockReach extends Module {
    public final NumberSetting distance = register(new NumberSetting("Distance", 10.0, 3.0, 20.0, 0.1));

    public BlockReach() {
        super("BlockReach", "Blok kırma/koyma mesafesini arttırır.", Category.MISC, Keyboard.KEY_NONE);
    }
    // Value used in MixinPlayerControllerMP
}
