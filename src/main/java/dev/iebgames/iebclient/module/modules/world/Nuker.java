package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class Nuker extends Module {

    public Nuker() {
        super("Nuker", "Etrafındaki blokları otomatik kırar.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        for (int x = -4; x <= 4; x++) {
            for (int y = -4; y <= 4; y++) {
                for (int z = -4; z <= 4; z++) {
                    BlockPos pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
                    Block block = mc.theWorld.getBlockState(pos).getBlock();
                    if (block != null && !block.getMaterial().isReplaceable()) {
                        mc.playerController.onPlayerDamageBlock(pos, mc.objectMouseOver.sideHit);
                        mc.thePlayer.swingItem();
                    }
                }
            }
        }
    }
}
