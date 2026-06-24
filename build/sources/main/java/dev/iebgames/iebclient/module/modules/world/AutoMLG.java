package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

public class AutoMLG extends Module {

    public AutoMLG() {
        super("AutoMLG", "Düşerken otomatik su kovası yerleştirir.", Category.WORLD, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer == null || mc.thePlayer.onGround || mc.thePlayer.motionY >= -0.5) return;
        if (mc.thePlayer.fallDistance < 3f) return;

        int bucketSlot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack s = mc.thePlayer.inventory.getStackInSlot(i);
            if (s != null && s.getItem() == Items.water_bucket) {
                bucketSlot = i;
                break;
            }
        }
        if (bucketSlot == -1) return;

        mc.thePlayer.inventory.currentItem = bucketSlot;
        BlockPos below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(),
                below, EnumFacing.UP, new Vec3(0.5, 1, 0.5));
    }
}
