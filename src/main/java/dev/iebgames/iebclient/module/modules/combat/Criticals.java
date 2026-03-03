package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventMotion;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.ModeSetting;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class Criticals extends Module {

    private final ModeSetting mode = register(new ModeSetting("Mode", "Packet", "Jump", "Spoof"));

    public Criticals() {
        super("Criticals", "Her saldırıda kritik vuruş zorlar.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onMotion(EventMotion e) {
        if (!e.isPre()) return;
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (!mc.thePlayer.onGround) return;
        if (mode.is("Packet")) {
            // Send two packets: slightly up, then back down to trigger critical
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
        } else if (mode.is("Jump")) {
            mc.thePlayer.jump();
        }
    }
}
