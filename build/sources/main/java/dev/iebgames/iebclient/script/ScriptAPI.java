package dev.iebgames.iebclient.script;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.util.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

/**
 * JavaScript bridge exposed as global {@code API} in every script.
 */
public class ScriptAPI {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public void chat(String message) {
        IEBClient.addChatMessage(message);
    }

    public void print(String message) {
        chat(message);
    }

    public boolean isInGame() {
        return mc.thePlayer != null && mc.theWorld != null;
    }

    public EntityPlayer getPlayer() {
        return mc.thePlayer;
    }

    public Minecraft getMc() {
        return mc;
    }

    public boolean isModuleEnabled(String name) {
        Module m = IEBClient.moduleManager.getModule(name);
        return m != null && m.isEnabled();
    }

    public void toggleModule(String name) {
        Module m = IEBClient.moduleManager.getModule(name);
        if (m != null) m.toggle();
    }

    public void enableModule(String name) {
        Module m = IEBClient.moduleManager.getModule(name);
        if (m != null) m.setEnabled(true);
    }

    public void disableModule(String name) {
        Module m = IEBClient.moduleManager.getModule(name);
        if (m != null) m.setEnabled(false);
    }

    public double getPlayerX() {
        return mc.thePlayer != null ? mc.thePlayer.posX : 0;
    }

    public double getPlayerY() {
        return mc.thePlayer != null ? mc.thePlayer.posY : 0;
    }

    public double getPlayerZ() {
        return mc.thePlayer != null ? mc.thePlayer.posZ : 0;
    }

    public float getPlayerYaw() {
        return mc.thePlayer != null ? mc.thePlayer.rotationYaw : 0;
    }

    public float getPlayerPitch() {
        return mc.thePlayer != null ? mc.thePlayer.rotationPitch : 0;
    }

    public void setSpeed(double speed) {
        if (mc.thePlayer != null) PlayerUtils.setSpeed(speed);
    }

    public boolean isMoving() {
        return mc.thePlayer != null && PlayerUtils.isMoving();
    }

    public int getBlockId(int x, int y, int z) {
        if (mc.theWorld == null) return 0;
        BlockPos pos = new BlockPos(x, y, z);
        return net.minecraft.block.Block.getIdFromBlock(mc.theWorld.getBlockState(pos).getBlock());
    }

    public void sendChat(String message) {
        if (mc.thePlayer != null) {
            mc.thePlayer.sendChatMessage(message);
        }
    }
}
