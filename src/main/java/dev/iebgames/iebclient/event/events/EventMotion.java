package dev.iebgames.iebclient.event.events;

import dev.iebgames.iebclient.event.Event;

public class EventMotion extends Event {

    public enum State { PRE, POST }

    private final State state;
    private float yaw, pitch;
    private boolean sprint;

    public EventMotion(State state, float yaw, float pitch, boolean sprint) {
        this.state  = state;
        this.yaw    = yaw;
        this.pitch  = pitch;
        this.sprint = sprint;
    }

    public State   getState()           { return state; }
    public float   getYaw()             { return yaw; }
    public void    setYaw(float yaw)    { this.yaw = yaw; }
    public float   getPitch()           { return pitch; }
    public void    setPitch(float p)    { this.pitch = p; }
    public boolean isSprint()           { return sprint; }
    public void    setSprint(boolean s) { this.sprint = s; }
    public boolean isPre()              { return state == State.PRE; }
    public boolean isPost()             { return state == State.POST; }
}
