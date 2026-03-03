package dev.iebgames.iebclient.event.events;

import dev.iebgames.iebclient.event.Event;
import net.minecraft.entity.Entity;

/** Fired when the local player attacks an entity. */
public class EventAttack extends Event {
    private Entity target;

    public EventAttack(Entity target) { this.target = target; }
    public Entity getTarget()          { return target; }
    public void   setTarget(Entity e)  { this.target = e; }
}
