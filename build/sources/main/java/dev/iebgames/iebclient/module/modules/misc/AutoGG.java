package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventPacket;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.input.Keyboard;

public class AutoGG extends Module {

    public AutoGG() {
        super("AutoGG", "Oyun bitince otomatik GG yazar.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onPacket(EventPacket e) {
        if (e.isSend() || mc.thePlayer == null) return;
        if (e.getPacket() instanceof S02PacketChat) {
            String msg = ((S02PacketChat) e.getPacket()).getChatComponent().getUnformattedText().toLowerCase();
            if (msg.contains("winner") || msg.contains("game over") || msg.contains("kazand") || msg.contains("1st") || msg.contains("won")) {
                mc.thePlayer.sendChatMessage("gg");
            }
        }
    }
}
