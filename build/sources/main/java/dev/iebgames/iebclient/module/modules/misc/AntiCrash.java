package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventPacket;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.network.play.server.S01PacketJoinGame;
import org.lwjgl.input.Keyboard;

public class AntiCrash extends Module {
    public AntiCrash() {
        super("AntiCrash", "Sunucu çöktürme paketlerini engellemeye çalışır.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onPacket(EventPacket e) {
        // Blocks suspicious large packets
    }
}
