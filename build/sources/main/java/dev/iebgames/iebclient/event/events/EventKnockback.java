package dev.iebgames.iebclient.event.events;

import dev.iebgames.iebclient.event.Event;

/** Fired when knockBack() is called on the local player. */
public class EventKnockback extends Event {
    private double motionX, motionZ;

    public EventKnockback(double motionX, double motionZ) {
        this.motionX = motionX;
        this.motionZ = motionZ;
    }

    public double getMotionX()              { return motionX; }
    public double getMotionZ()              { return motionZ; }
    public void   setMotionX(double mx)     { this.motionX = mx; }
    public void   setMotionZ(double mz)     { this.motionZ = mz; }
}
