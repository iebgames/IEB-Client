package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventRender3D;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import dev.iebgames.iebclient.util.EspRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class BlockESP extends Module {

    private final NumberSetting range = register(new NumberSetting("Range", 30, 10, 64, 1));

    public BlockESP() {
        super("BlockESP", "Değerli blokları vurgular (elmas, spawner, yatak).", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onRender3D(EventRender3D e) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        BlockPos center = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        int r = range.getInt();
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = center.add(x, y, z);
                    Block block = mc.theWorld.getBlockState(pos).getBlock();
                    int color = 0;
                    if (block == Blocks.diamond_ore || block == Blocks.emerald_ore) color = 0xAA00FFFF;
                    else if (block == Blocks.mob_spawner) color = 0xAAFF00FF;
                    else if (block == Blocks.bed) color = 0xAFFF0000;
                    else if (block == Blocks.gold_ore || block == Blocks.iron_ore) color = 0xAFFFFF00;
                    if (color != 0) EspRenderHelper.drawBlockBox(pos.getX(), pos.getY(), pos.getZ(), color, e.getPartialTicks());
                }
            }
        }
    }
}
