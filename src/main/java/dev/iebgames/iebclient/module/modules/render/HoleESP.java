package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventRender3D;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import dev.iebgames.iebclient.util.EspRenderHelper;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class HoleESP extends Module {

    private final NumberSetting range = register(new NumberSetting("Range", 20, 5, 50, 1));

    public HoleESP() {
        super("HoleESP", "Güvenli 1x1 delikleri gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onRender3D(EventRender3D e) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        BlockPos center = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        int r = range.getInt();
        for (int x = -r; x <= r; x++) {
            for (int z = -r; z <= r; z++) {
                for (int y = -3; y <= 1; y++) {
                    BlockPos pos = center.add(x, y, z);
                    if (isSafeHole(pos)) {
                        EspRenderHelper.drawBlockBox(pos.getX(), pos.getY(), pos.getZ(), 0xAA00FF00, e.getPartialTicks());
                    }
                }
            }
        }
    }

    private boolean isSafeHole(BlockPos pos) {
        if (!mc.theWorld.isAirBlock(pos)) return false;
        BlockPos[] sides = {pos.north(), pos.south(), pos.east(), pos.west(), pos.down()};
        for (BlockPos side : sides) {
            if (mc.theWorld.isAirBlock(side)) return false;
        }
        return true;
    }
}
