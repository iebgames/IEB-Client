package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class AutoArmor extends Module {

    public AutoArmor() {
        super("AutoArmor", "En iyi zırhları otomatik giyer.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.currentScreen instanceof GuiInventory || mc.currentScreen == null) {
            // Basic logic: compare protection values and equip
        }
    }
}
