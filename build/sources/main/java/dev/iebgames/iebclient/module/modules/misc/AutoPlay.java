package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventPacket;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.ModeSetting;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.input.Keyboard;

public class AutoPlay extends Module {

    private final ModeSetting mode = register(new ModeSetting("Mode", "SkyWars", "BedWars", "Duels"));

    public AutoPlay() {
        super("AutoPlay", "Oyun bitince yeni oyuna girer.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onPacket(EventPacket e) {
        if (e.isSend() || mc.thePlayer == null) return;
        if (e.getPacket() instanceof S02PacketChat) {
            String msg = ((S02PacketChat) e.getPacket()).getChatComponent().getUnformattedText().toLowerCase();
            if (msg.contains("play again") || msg.contains("tekrar oyna") || msg.contains("click here")) {
                mc.thePlayer.sendChatMessage("/play " + mode.getValue().toLowerCase());
            }
        }
    }
}
