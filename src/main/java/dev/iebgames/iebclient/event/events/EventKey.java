package dev.iebgames.iebclient.event.events;

import dev.iebgames.iebclient.event.Event;

/** Fired when a keyboard key is pressed. */
public class EventKey extends Event {
    private final int keyCode;

    public EventKey(int keyCode) { this.keyCode = keyCode; }
    public int getKeyCode()      { return keyCode; }
}
