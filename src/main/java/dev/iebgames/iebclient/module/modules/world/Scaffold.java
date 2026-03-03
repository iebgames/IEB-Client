package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

public class Scaffold extends Module {

    public Scaffold() {
        super("Scaffold", "Altına otomatik blok koyar.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(pos).getBlock().getMaterial().isReplaceable()) {
            int slot = getBlockSlot();
            if (slot != -1) {
                int oldSlot = mc.thePlayer.inventory.currentItem;
                mc.thePlayer.inventory.currentItem = slot;
                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, EnumFacing.UP, new Vec3(pos.getX(), pos.getY(), pos.getZ()));
                mc.thePlayer.swingItem();
                mc.thePlayer.inventory.currentItem = oldSlot;
            }
        }
    }

    private int getBlockSlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.thePlayer.inventory.getStackInSlot(i) != null && mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                return i;
            }
        }
        return -1;
    }
}
