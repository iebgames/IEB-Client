package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.lwjgl.input.Keyboard;

public class Stealer extends Module {
    public Stealer() {
        super("Stealer", "Containerlardaki eşyaları hızlıca çalar.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer.windowId != 0) {
            Container container = mc.thePlayer.openContainer;
            for (int i = 0; i < container.inventorySlots.size() - 36; i++) {
                Slot slot = container.getSlot(i);
                if (slot.getHasStack()) {
                    mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
                    return; // One per tick for bypass
                }
            }
        }
    }
}
