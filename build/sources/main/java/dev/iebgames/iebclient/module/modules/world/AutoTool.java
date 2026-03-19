package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class AutoTool extends Module {

    public AutoTool() {
        super("AutoTool", "Kıracağın bloğa göre en iyi aleti seçer.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.gameSettings.keyBindAttack.isKeyDown() && mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            Block block = mc.theWorld.getBlockState(pos).getBlock();
            float strength = 1.0f;
            int bestSlot = -1;
            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                if (stack != null && stack.getStrVsBlock(block) > strength) {
                    strength = stack.getStrVsBlock(block);
                    bestSlot = i;
                }
            }
            if (bestSlot != -1) mc.thePlayer.inventory.currentItem = bestSlot;
        }
    }
}
