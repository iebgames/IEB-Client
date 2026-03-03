package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventKnockback;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {

    private final NumberSetting horizontal = register(new NumberSetting("Horizontal %", 40, 0, 100, 1));
    private final NumberSetting vertical   = register(new NumberSetting("Vertical %",   60, 0, 100, 1));

    public Velocity() {
        super("Velocity", "Knockback'i azaltır.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onKnockback(EventKnockback e) {
        e.setMotionX(e.getMotionX() * (horizontal.getValue() / 100.0));
        e.setMotionZ(e.getMotionZ() * (horizontal.getValue() / 100.0));
    }
}
