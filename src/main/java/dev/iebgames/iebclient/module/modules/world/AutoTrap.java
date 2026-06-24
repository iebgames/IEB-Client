package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.util.PlayerUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

public class AutoTrap extends Module {

    public AutoTrap() {
        super("AutoTrap", "Düşman etrafına blok yerleştirir.", Category.WORLD, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        EntityLivingBase target = PlayerUtils.getClosestTarget(5, true, false);
        if (target == null) return;
        int slot = findBlock();
        if (slot == -1) return;
        mc.thePlayer.inventory.currentItem = slot;

        BlockPos base = new BlockPos(target.posX, target.posY, target.posZ);
        BlockPos[] positions = {base.north(), base.south(), base.east(), base.west(), base.up()};
        for (BlockPos pos : positions) {
            if (mc.theWorld.isAirBlock(pos)) {
                BlockPos below = pos.down();
                if (!mc.theWorld.isAirBlock(below)) {
                    mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(),
                            below, EnumFacing.UP, new Vec3(0.5, 1, 0.5));
                    return;
                }
            }
        }
    }

    private int findBlock() {
        for (int i = 0; i < 9; i++) {
            ItemStack s = mc.thePlayer.inventory.getStackInSlot(i);
            if (s != null && s.getItem() instanceof ItemBlock) return i;
        }
        return -1;
    }
}
