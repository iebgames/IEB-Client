package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventPacket;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class Disabler extends Module {
    public Disabler() {
        super("Disabler", "Anti-Cheat korumalarını devre dışı bırakmayı dener.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onPacket(EventPacket e) {
        if (e.isSend() && e.getPacket() instanceof C03PacketPlayer) {
            // Basic disabler: sometimes spoofing motion or cancelling certain field updates works
        }
    }
}
