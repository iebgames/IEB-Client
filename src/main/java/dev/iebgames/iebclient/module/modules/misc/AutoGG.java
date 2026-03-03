package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class AutoGG extends Module {
    public AutoGG() {
        super("AutoGG", "Oyun bitince otomatik 'GG' yazar.", Category.MISC, Keyboard.KEY_NONE);
    }
    // Logic: scanning chat for "Winner" or similar keywords
}
