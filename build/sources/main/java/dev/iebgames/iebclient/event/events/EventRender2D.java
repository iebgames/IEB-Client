package dev.iebgames.iebclient.event.events;

import dev.iebgames.iebclient.event.Event;
import net.minecraft.client.gui.ScaledResolution;

/** Fired during renderGameOverlay for HUD drawing */
public class EventRender2D extends Event {
    private final ScaledResolution sr;
    private final float partialTicks;

    public EventRender2D() {
        this.sr = null;
        this.partialTicks = 0;
    }

    public EventRender2D(ScaledResolution sr, float partialTicks) {
        this.sr           = sr;
        this.partialTicks = partialTicks;
    }

    public ScaledResolution getScaledResolution() { return sr; }
    public float            getPartialTicks()      { return partialTicks; }
}
