package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventPacket;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.network.play.client.C02PacketUseEntity;
import org.lwjgl.input.Keyboard;

public class Reach extends Module {

    private final NumberSetting range = register(new NumberSetting("Range", 10.0, 3.0, 20.0, 0.1));

    public Reach() {
        super("Reach", "Saldırı menzilini arttırır.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onPacket(EventPacket e) {
        // Reach works by allowing attack packets from further away.
        // The actual range bypass is handled by the Mixin on EntityPlayerSP.
        // Here we ensure attack packets (C02) aren't cancelled.
        if (!e.isSend()) return;
        if (e.getPacket() instanceof C02PacketUseEntity) {
            e.setCancelled(false);
        }
    }

    public double getRange() { return range.getValue(); }
}
