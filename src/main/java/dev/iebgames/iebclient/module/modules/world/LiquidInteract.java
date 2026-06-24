package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

public class LiquidInteract extends Module {

    private final NumberSetting reach = register(new NumberSetting("Reach", 4.5, 3.0, 6.0, 0.1));

    public LiquidInteract() {
        super("LiquidInteract", "Sıvı içine blok koymanı sağlar.", Category.WORLD, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer == null || mc.objectMouseOver == null) return;
        if (mc.objectMouseOver.typeOfHit != net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK) return;
        if (!(mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) return;

        BlockPos pos = mc.objectMouseOver.getBlockPos();
        if (!mc.theWorld.getBlockState(pos).getBlock().getMaterial().isLiquid()) return;
        if (mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ()) > reach.getValue()) return;

        EnumFacing face = mc.objectMouseOver.sideHit;
        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, face, new Vec3(0.5, 0.5, 0.5));
        mc.thePlayer.swingItem();
    }
}
