package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class AutoMine extends Module {

    public AutoMine() {
        super("AutoMine", "Otomatik blok kazar.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        ((dev.iebgames.iebclient.mixin.KeyBindingAccessor)mc.gameSettings.keyBindAttack).setPressed(true);
    }

    @Override
    public void onDisable() {
        ((dev.iebgames.iebclient.mixin.KeyBindingAccessor)mc.gameSettings.keyBindAttack).setPressed(Keyboard.isKeyDown(mc.gameSettings.keyBindAttack.getKeyCode()));
    }
}
