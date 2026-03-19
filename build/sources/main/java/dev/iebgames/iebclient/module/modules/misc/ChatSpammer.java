package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.util.TimerUtils;
import org.lwjgl.input.Keyboard;

public class ChatSpammer extends Module {
    private final TimerUtils timer = new TimerUtils();

    public ChatSpammer() {
        super("ChatSpammer", "Sohbete otomatik yazı yazar.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (timer.hasReached(3000)) {
            mc.thePlayer.sendChatMessage("IEB Client on top! (BY IEB GAMES)");
            timer.reset();
        }
    }
}
