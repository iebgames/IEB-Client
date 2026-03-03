package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class AutoTotem extends Module {

    private final NumberSetting health = register(new NumberSetting("Health", 10.0, 1.0, 20.0, 1.0));

    public AutoTotem() {
        super("AutoTotem", "Envanterde totem varsa otomatik eline alır (1.8.9'da totem yoktur, placeholder).", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        // 1.8.9 doesn't have totems, so this is a placeholder or for servers with custom items.
        // We'll search for an item that matches "totem" if it existed.
        if (mc.thePlayer.getHealth() <= health.getValue()) {
            for (int i = 0; i < 36; i++) {
                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                if (stack != null && stack.getItem().getUnlocalizedName().contains("totem")) {
                    // Logic to swap to offhand (which doesn't exist in 1.8.9) or main hand
                    mc.thePlayer.inventory.currentItem = i < 9 ? i : mc.thePlayer.inventory.currentItem;
                    break;
                }
            }
        }
    }
}
