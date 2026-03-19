package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class AutoHeal extends Module {

    private final NumberSetting health = register(new NumberSetting("Health", 15.0, 1.0, 20.0, 1.0));

    public AutoHeal() {
        super("AutoHeal", "Canın azaldığında otomatik Gapple yer.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.getHealth() <= health.getValue()) {
            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                if (stack != null && stack.getItem() == Items.golden_apple) {
                    int oldSlot = mc.thePlayer.inventory.currentItem;
                    mc.thePlayer.inventory.currentItem = i;
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, stack);
                    mc.thePlayer.inventory.currentItem = oldSlot;
                    break;
                }
            }
        }
    }
}
