package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.util.TimerUtils;
import org.lwjgl.input.Keyboard;

public class AntiAFK extends Module {
    private final TimerUtils timer = new TimerUtils();

    public AntiAFK() {
        super("Anti-AFK", "Sunucudan atılmamak için hareket eder.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (timer.hasReached(5000)) {
            mc.thePlayer.jump();
            timer.reset();
        }
    }
}
