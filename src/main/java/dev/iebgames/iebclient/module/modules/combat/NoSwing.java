package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventPacket;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.network.play.client.C0APacketAnimation;
import org.lwjgl.input.Keyboard;

public class NoSwing extends Module {

    public NoSwing() {
        super("NoSwing", "Vururken el hareketini (swing) gizler.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof C0APacketAnimation) {
            e.setCancelled(true);
        }
    }
}
