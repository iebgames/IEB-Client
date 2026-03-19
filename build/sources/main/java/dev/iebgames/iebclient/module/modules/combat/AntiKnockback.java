package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventKnockback;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class AntiKnockback extends Module {

    public AntiKnockback() {
        super("Anti-Knockback", "Knockback'i tamamen engeller.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onKnockback(EventKnockback e) {
        e.setMotionX(0);
        e.setMotionZ(0);
        e.cancel();
    }
}
