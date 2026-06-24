package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.block.BlockBed;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class BedAura extends Module {

    private final NumberSetting range = register(new NumberSetting("Range", 4.5, 3.0, 6.0, 0.1));

    public BedAura() {
        super("BedAura", "Yakındaki yatakları otomatik kırar.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        int r = (int) Math.ceil(range.getValue());
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockBed) {
                        if (mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= range.getValue()) {
                            mc.playerController.clickBlock(pos, net.minecraft.util.EnumFacing.UP);
                            mc.thePlayer.swingItem();
                            return;
                        }
                    }
                }
            }
        }
    }
}
