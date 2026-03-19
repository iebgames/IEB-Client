package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.mixin.KeyBindingAccessor;
import org.lwjgl.input.Keyboard;

public class AutoWalk extends Module {

    public AutoWalk() {
        super("AutoWalk", "Otomatik olarak ileriye doğru yürür.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        ((KeyBindingAccessor) mc.gameSettings.keyBindForward).setPressed(true);
    }

    @Override
    protected void onDisable() {
        ((KeyBindingAccessor) mc.gameSettings.keyBindForward).setPressed(Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()));
    }
}
