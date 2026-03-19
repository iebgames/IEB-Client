package dev.iebgames.iebclient.module.modules.player;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import dev.iebgames.iebclient.util.TimerUtils;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class InventoryCleaner extends Module {

    private final NumberSetting delay = register(new NumberSetting("Delay", 100, 50, 500, 10));
    private final TimerUtils timer = new TimerUtils();

    public InventoryCleaner() {
        super("InventoryCleaner", "Gereksiz eşyaları otomatik atar.", Category.PLAYER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.currentScreen != null) return;

        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBad(stack)) {
                    if (timer.hasReached(delay.getLong())) {
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 1, 4, mc.thePlayer);
                        timer.reset();
                    }
                    return;
                }
            }
        }
    }

    private boolean isBad(ItemStack stack) {
        if (stack == null) return false;
        String name = stack.getItem().getUnlocalizedName();
        return name.contains("rotten_flesh") || name.contains("poisonous_potato") 
            || name.contains("string") || name.contains("stick");
    }
}
