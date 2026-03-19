package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventPacket;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.network.Packet;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class Blink extends Module {
    private final List<Packet<?>> packets = new ArrayList<>();

    public Blink() {
        super("Blink", "Paketleri bekletip ışınlanmış gibi gösterir.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onPacket(EventPacket e) {
        if (e.isSend()) {
            e.cancel();
            packets.add(e.getPacket());
        }
    }

    @Override
    protected void onDisable() {
        for (Packet<?> p : packets) {
            mc.getNetHandler().addToSendQueue(p);
        }
        packets.clear();
    }
}
