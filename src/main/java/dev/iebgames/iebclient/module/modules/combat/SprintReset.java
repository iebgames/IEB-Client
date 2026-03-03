package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventAttack;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.lwjgl.input.Keyboard;

public class SprintReset extends Module {

    public SprintReset() {
        super("SprintReset", "Vurduğunda sprinti sıfırlayarak kb verir.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onAttack(EventAttack e) {
        if (mc.thePlayer.isSprinting()) {
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
        }
    }
}
