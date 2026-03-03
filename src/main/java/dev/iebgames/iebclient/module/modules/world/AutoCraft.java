package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class AutoCraft extends Module {
    public AutoCraft() {
        super("AutoCraft", "Hızlıca eşya üretmeni sağlar.", Category.MISC, Keyboard.KEY_NONE);
    }
    // Logic will be triggered via GUI interactions or packet manipulation
}
