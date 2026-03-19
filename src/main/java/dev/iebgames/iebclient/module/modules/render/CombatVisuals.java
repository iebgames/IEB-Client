package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.BooleanSetting;
import org.lwjgl.input.Keyboard;

public class CombatVisuals extends Module {

    public final BooleanSetting hitColor = register(new BooleanSetting("Hit Color", true));
    public final BooleanSetting hitBubbles = register(new BooleanSetting("Hit Bubbles", false));

    public CombatVisuals() {
        super("CombatVisuals", "Savaş sırasındaki görsel efektleri özelleştirir.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
