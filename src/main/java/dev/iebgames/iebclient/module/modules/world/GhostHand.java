package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class GhostHand extends Module {
    public GhostHand() {
        super("GhostHand", "Blokların arkasındaki sandıklara sağ tıklamanı sağlar.", Category.MISC, Keyboard.KEY_NONE);
    }
    // Logic handled in MixinRenderGlobal or MixinMinecraft mouseover logic
}
