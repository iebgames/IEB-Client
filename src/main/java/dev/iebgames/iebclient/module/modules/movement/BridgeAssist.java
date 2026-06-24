package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.mixin.KeyBindingAccessor;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class BridgeAssist extends Module {

    public BridgeAssist() {
        super("BridgeAssist", "Sneak and place blocks at edges while bridging.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (!mc.thePlayer.onGround) return;

        BlockPos below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        BlockPos ahead = below.offset(mc.thePlayer.getHorizontalFacing());
        boolean edge = mc.theWorld.isAirBlock(below) || mc.theWorld.isAirBlock(ahead);

        KeyBindingAccessor sneak = (KeyBindingAccessor) mc.gameSettings.keyBindSneak;
        KeyBindingAccessor use = (KeyBindingAccessor) mc.gameSettings.keyBindUseItem;

        if (edge && hasBlockInHotbar()) {
            sneak.setPressed(true);
            if (mc.thePlayer.isSneaking()) {
                use.setPressed(true);
            }
        } else {
            sneak.setPressed(false);
        }
    }

    @Override
    protected void onDisable() {
        ((KeyBindingAccessor) mc.gameSettings.keyBindSneak).setPressed(false);
        ((KeyBindingAccessor) mc.gameSettings.keyBindUseItem).setPressed(false);
    }

    private boolean hasBlockInHotbar() {
        for (int i = 0; i < 9; i++) {
            ItemStack s = mc.thePlayer.inventory.getStackInSlot(i);
            if (s != null && s.getItem() instanceof ItemBlock) return true;
        }
        return false;
    }
}
