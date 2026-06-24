package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventPacket;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.input.Keyboard;

public class AutoQueue extends Module {

    public AutoQueue() {
        super("AutoQueue", "Oyun bitince sıradaki oyuna girer.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onPacket(EventPacket e) {
        if (e.isSend() || mc.thePlayer == null) return;
        if (e.getPacket() instanceof S02PacketChat) {
            String msg = ((S02PacketChat) e.getPacket()).getChatComponent().getUnformattedText().toLowerCase();
            if (msg.contains("queue") || msg.contains("requeue") || msg.contains("play again")) {
                mc.thePlayer.sendChatMessage("/requeue");
            }
        }
    }
}
