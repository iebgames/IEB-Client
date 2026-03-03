package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class FastEat extends Module {
    public FastEat() {
        super("FastEat", "Yemekleri anında ye.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem().getItem().getItemUseAction(mc.thePlayer.getHeldItem()) == net.minecraft.item.EnumAction.EAT) {
            for (int i = 0; i < 20; i++) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
            }
        }
    }
}
