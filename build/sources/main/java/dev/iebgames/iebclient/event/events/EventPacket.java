package dev.iebgames.iebclient.event.events;

import dev.iebgames.iebclient.event.Event;
import net.minecraft.network.Packet;

/** Fired when a packet is sent or received. Cancellable = drop packet. */
public class EventPacket extends Event {

    public enum Direction { SEND, RECEIVE }

    private final Direction direction;
    private Packet<?> packet;

    public EventPacket(Packet<?> packet, boolean send) {
        this.direction = send ? Direction.SEND : Direction.RECEIVE;
        this.packet = packet;
    }

    public Direction  getDirection() { return direction; }
    public Packet<?>  getPacket()    { return packet; }
    public void       setPacket(Packet<?> p) { this.packet = p; }
    public boolean    isSend()       { return direction == Direction.SEND; }
    public boolean    isReceive()    { return direction == Direction.RECEIVE; }
}
