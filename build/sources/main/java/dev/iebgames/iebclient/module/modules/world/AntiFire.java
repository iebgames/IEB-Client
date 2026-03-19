package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class AntiFire extends Module {
    public AntiFire() {
        super("AntiFire", "Yandığında otomatik söndürür (Packet based).", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.isBurning()) {
            // Logic to send position packets to "reset" burning state or similar server-side hacks
        }
    }
}
