package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventPacket;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class PingSpoof extends Module {
    private final NumberSetting delay = register(new NumberSetting("Delay", 100, 10, 1000, 10));

    public PingSpoof() {
        super("PingSpoof", "Pingini sunucuya farklı gösterir.", Category.MISC, Keyboard.KEY_NONE);
    }
    // Handled similarly to FakeLag but specifically for keep-alive packets
}
