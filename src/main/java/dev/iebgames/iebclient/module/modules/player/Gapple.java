package dev.iebgames.iebclient.module.modules.player;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class Gapple extends Module {

    private final NumberSetting health = register(new NumberSetting("Health", 15.0, 1.0, 20.0, 0.5));

    public Gapple() {
        super("Gapple", "Canın azaldığında otomatik altın elma yer.", Category.PLAYER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.getHealth() <= health.getValue()) {
            int slot = findGapple();
            if (slot != -1) {
                int oldSlot = mc.thePlayer.inventory.currentItem;
                mc.thePlayer.inventory.currentItem = slot;
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                mc.thePlayer.inventory.currentItem = oldSlot;
            }
        }
    }

    private int findGapple() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
            if (stack != null && stack.getItem() instanceof ItemAppleGold) return i;
        }
        return -1;
    }
}
