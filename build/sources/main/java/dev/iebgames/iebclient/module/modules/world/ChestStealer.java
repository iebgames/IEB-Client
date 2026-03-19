package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import org.lwjgl.input.Keyboard;

public class ChestStealer extends Module {

    public ChestStealer() {
        super("ChestStealer", "Sandıktaki eşyaları otomatik toplar.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) mc.currentScreen;
            ContainerChest container = (ContainerChest) chest.inventorySlots;
            for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); i++) {
                if (container.getLowerChestInventory().getStackInSlot(i) != null) {
                    mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
                }
            }
        }
    }
}
