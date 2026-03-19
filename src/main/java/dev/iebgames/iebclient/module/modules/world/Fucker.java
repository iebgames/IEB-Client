package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.block.BlockCake;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;

public class Fucker extends Module {

    private final NumberSetting range = register(new NumberSetting("Range", 4.5, 1.0, 6.0, 0.1));

    public Fucker() {
        super("Fucker", "Çevrendeki yatak ve yumurta gibi blokları otomatik kırar.", Category.WORLD, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        int r = range.getInt();
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
                    if (isTarget(pos)) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
                        mc.thePlayer.swingItem();
                        return;
                    }
                }
            }
        }
    }

    private boolean isTarget(BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock() instanceof BlockBed || 
               mc.theWorld.getBlockState(pos).getBlock() instanceof BlockDragonEgg ||
               mc.theWorld.getBlockState(pos).getBlock() instanceof BlockCake;
    }
}
