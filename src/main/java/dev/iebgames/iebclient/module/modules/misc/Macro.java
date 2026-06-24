package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventKey;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import dev.iebgames.iebclient.setting.TextSetting;
import org.lwjgl.input.Keyboard;

public class Macro extends Module {

    private final TextSetting command = register(new TextSetting("Command", "/spawn"));
    private final NumberSetting key = register(new NumberSetting("Key", Keyboard.KEY_NONE, 0, Keyboard.KEY_RMENU, 1));

    public Macro() {
        super("Macro", "Tuşa basınca komut çalıştırır.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onKey(EventKey e) {
        if (e.getKey() == key.getInt() && mc.thePlayer != null) {
            mc.thePlayer.sendChatMessage(command.getValue());
        }
    }
}
