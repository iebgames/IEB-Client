package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventPacket;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.util.TimerUtils;
import net.minecraft.network.Packet;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class FakeLag extends Module {
    private final List<Packet<?>> packets = new ArrayList<>();
    private final TimerUtils timer = new TimerUtils();

    public FakeLag() {
        super("FakeLag", "Yapay lag oluşturur.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onPacket(EventPacket e) {
        if (e.isSend()) {
            e.cancel();
            packets.add(e.getPacket());
            if (timer.hasReached(500)) {
                for (Packet<?> p : packets) {
                    mc.getNetHandler().addToSendQueue(p);
                }
                packets.clear();
                timer.reset();
            }
        }
    }
}
