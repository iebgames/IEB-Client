package dev.iebgames.iebclient.module.modules.player;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class FastUse extends Module {

    private final NumberSetting speed = register(new NumberSetting("Speed", 20.0, 1.0, 100.0, 1.0));

    public FastUse() {
        super("FastUse", "Eşyaları (yemek, iksir vb.) hızlı kullanmanı sağlar.", Category.PLAYER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.isUsingItem()) {
            for (int i = 0; i < speed.getInt(); i++) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
            }
        }
    }
}
