package dev.iebgames.iebclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public final class PacketUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void sendPacket(Packet<?> packet) {
        if (mc.thePlayer != null && mc.thePlayer.sendQueue != null) {
            mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
    }
}
