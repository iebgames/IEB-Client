package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.inventory.Slot;
import org.lwjgl.input.Keyboard;

public class AutoCraft extends Module {

    public AutoCraft() {
        super("AutoCraft", "Crafting masasında hızlı üretim yapar.", Category.WORLD, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (!(mc.currentScreen instanceof GuiCrafting) || mc.thePlayer.openContainer == null) return;
        for (int i = 0; i < mc.thePlayer.openContainer.inventorySlots.size(); i++) {
            Slot slot = mc.thePlayer.openContainer.getSlot(i);
            if (slot.getHasStack() && slot.inventory != mc.thePlayer.inventory) {
                mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, i, 0, 1, mc.thePlayer);
                return;
            }
        }
    }
}
