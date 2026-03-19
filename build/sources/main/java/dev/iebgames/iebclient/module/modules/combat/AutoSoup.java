package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import org.lwjgl.input.Keyboard;

public class AutoSoup extends Module {

    private final NumberSetting health = register(new NumberSetting("Health", 13.0, 1.0, 20.0, 1.0));

    public AutoSoup() {
        super("AutoSoup", "Canın azaldığında otomatik çorba içer.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.getHealth() <= health.getValue()) {
            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                if (stack != null && stack.getItem() == Items.mushroom_stew) {
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(i));
                    mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(stack));
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    break;
                }
            }
        }
    }
}
