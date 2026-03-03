package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.input.Keyboard;

public class InventoryMove extends Module {

    public InventoryMove() {
        super("InventoryMove", "Envanter açıkken hareket etmeni sağlar.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.currentScreen instanceof GuiContainer) {
            Keyboard.enableRepeatEvents(true);
            // In a real implementation, we would bridge the key binds to the movement logic
        }
    }
}
