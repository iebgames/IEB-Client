package dev.iebgames.iebclient.module.modules.player;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import dev.iebgames.iebclient.mixin.KeyBindingAccessor;

public class Eagle extends Module {

    public Eagle() {
        super("Eagle", "Blok koyarken kenarlarda otomatik çömelir.", Category.PLAYER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() instanceof BlockAir && mc.thePlayer.onGround) {
            ((KeyBindingAccessor) mc.gameSettings.keyBindSneak).setPressed(true);
        } else {
            ((KeyBindingAccessor) mc.gameSettings.keyBindSneak).setPressed(Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()));
        }
    }

    @Override
    protected void onDisable() {
        ((KeyBindingAccessor) mc.gameSettings.keyBindSneak).setPressed(Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()));
    }
}
