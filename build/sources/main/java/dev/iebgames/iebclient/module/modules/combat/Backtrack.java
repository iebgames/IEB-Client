package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventPacket;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class Backtrack extends Module {

    private final NumberSetting delay = register(new NumberSetting("Delay", 200.0, 50.0, 1000.0, 50.0));
    private final List<Packet<?>> packets = new ArrayList<>();

    public Backtrack() {
        super("Backtrack", "Gelen paketleri geciktirerek lag oluşturur.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onPacket(EventPacket e) {
        if (e.isReceive()) {
            if (e.getPacket() instanceof S14PacketEntity) {
                e.cancel();
                packets.add(e.getPacket());
                // In a real implementation, we'd release these after the delay
            }
        }
    }
}
