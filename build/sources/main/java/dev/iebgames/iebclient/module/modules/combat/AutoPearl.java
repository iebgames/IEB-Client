package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class AutoPearl extends Module {

    public AutoPearl() {
        super("AutoPearl", "Void'e düşerken otomatik ender pearl atar.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer == null) return;
        if (mc.thePlayer.posY > 0 || mc.thePlayer.onGround) return;

        int pearlSlot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack s = mc.thePlayer.inventory.getStackInSlot(i);
            if (s != null && s.getItem() == Items.ender_pearl) {
                pearlSlot = i;
                break;
            }
        }
        if (pearlSlot == -1) return;

        mc.thePlayer.inventory.currentItem = pearlSlot;
        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
    }
}
