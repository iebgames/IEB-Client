package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

public class AutoBuild extends Module {

    private final NumberSetting delay = register(new NumberSetting("Delay", 3, 1, 10, 1));
    private int tickWait;

    public AutoBuild() {
        super("AutoBuild", "Otomatik yapı inşa eder (ileri doğru çizgi).", Category.WORLD, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (tickWait++ < delay.getInt()) return;
        tickWait = 0;

        int slot = findBlockSlot();
        if (slot == -1) return;
        mc.thePlayer.inventory.currentItem = slot;

        float yaw = mc.thePlayer.rotationYaw;
        double rad = Math.toRadians(yaw);
        BlockPos target = new BlockPos(
                mc.thePlayer.posX - Math.sin(rad) * 2,
                mc.thePlayer.posY - 1,
                mc.thePlayer.posZ + Math.cos(rad) * 2
        );

        if (mc.theWorld.getBlockState(target).getBlock().isFullBlock()) return;
        BlockPos neighbor = target.down();
        if (!mc.theWorld.getBlockState(neighbor).getBlock().isFullBlock()) return;

        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(),
                neighbor, EnumFacing.UP, new Vec3(0.5, 1, 0.5));
        mc.thePlayer.swingItem();
    }

    private int findBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBlock) {
                Block b = ((ItemBlock) stack.getItem()).getBlock();
                if (b.isFullBlock()) return i;
            }
        }
        return -1;
    }
}
