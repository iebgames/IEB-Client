package dev.iebgames.iebclient.event.events;

import dev.iebgames.iebclient.event.Event;

public class EventMotion extends Event {

    private double x, y, z;
    private float yaw, pitch;
    private boolean ground, pre;

    public EventMotion(float yaw, float pitch, double x, double y, double z, boolean ground, boolean pre) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.x = x;
        this.y = y;
        this.z = z;
        this.ground = ground;
        this.pre = pre;
    }

    public double getX()              { return x; }
    public void   setX(double x)      { this.x = x; }
    public double getY()              { return y; }
    public void   setY(double y)      { this.y = y; }
    public double getZ()              { return z; }
    public void   setZ(double z)      { this.z = z; }

    public float  getYaw()            { return yaw; }
    public void   setYaw(float yaw)   { this.yaw = yaw; }
    public float  getPitch()          { return pitch; }
    public void   setPitch(float p)   { this.pitch = p; }

    public boolean isOnGround()       { return ground; }
    public void    setOnGround(boolean g) { this.ground = g; }

    public boolean isPre()            { return pre; }
    public boolean isPost()           { return !pre; }
}
