package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventPacket;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.input.Keyboard;

public class AutoL extends Module {

    public AutoL() {
        super("AutoL", "Bir oyuncuyu öldürdüğünde sohbete otomatik L mesajı atar.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof S02PacketChat) {
            S02PacketChat p = (S02PacketChat) e.getPacket();
            String msg = p.getChatComponent().getUnformattedText();
            // Detect common death messages containing the player's name and 'by mc.thePlayer'
            if (msg.contains("killed by " + mc.thePlayer.getName()) || msg.contains("slain by " + mc.thePlayer.getName())) {
                mc.thePlayer.sendChatMessage("L / Git Gud");
            }
        }
    }
}
