package dev.iebgames.iebclient.event.events;

import dev.iebgames.iebclient.event.Event;

public class EventRender3D extends Event {
    private final float partialTicks;

    public EventRender3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
