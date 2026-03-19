package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

public class Scaffold extends Module {

    private final NumberSetting expand = register(new NumberSetting("Expand", 1.0, 0.0, 5.0, 0.1));

    public Scaffold() {
        super("Scaffold", "Altına otomatik blok koyar (Genişletme modu dahil).", Category.WORLD, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        double x = mc.thePlayer.posX + (mc.thePlayer.motionX * expand.getValue());
        double z = mc.thePlayer.posZ + (mc.thePlayer.motionZ * expand.getValue());
        BlockPos pos = new BlockPos(x, mc.thePlayer.posY - 1, z);
        
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
