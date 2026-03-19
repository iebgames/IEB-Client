package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class AntiVoid extends Module {

    private final NumberSetting fallDist = register(new NumberSetting("Fall Distance", 5.0, 1.0, 20.0, 0.1));

    public AntiVoid() {
        super("AntiVoid", "Boşluğa düşmeni engeller.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.fallDistance > fallDist.getValue()) {
            boolean overVoid = true;
            for (int i = (int) mc.thePlayer.posY; i > 0; i--) {
                if (!(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ)).getBlock() instanceof BlockAir)) {
                    overVoid = false;
                    break;
                }
            }

            if (overVoid) {
                mc.thePlayer.motionY = 0;
                mc.thePlayer.fallDistance = 0;
            }
        }
    }
}
