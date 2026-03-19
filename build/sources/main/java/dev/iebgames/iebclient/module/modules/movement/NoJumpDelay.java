package dev.iebgames.iebclient.module.modules.movement;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.mixin.EntityLivingBaseAccessor;
import org.lwjgl.input.Keyboard;

public class NoJumpDelay extends Module {

    public NoJumpDelay() {
        super("NoJumpDelay", "Zıplama aralığındaki bekleme süresini kaldırır.", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        // We set the jump ticks to 0 to allow continuous jumping
        ((EntityLivingBaseAccessor) mc.thePlayer).setJumpTicks(0);
    }
}
