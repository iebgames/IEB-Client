package dev.iebgames.iebclient.module.modules.player;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.mixin.MinecraftAccessor;
import dev.iebgames.iebclient.mixin.EntityLivingBaseAccessor;
import org.lwjgl.input.Keyboard;

public class DelayRemover extends Module {

    public DelayRemover() {
        super("DelayRemover", "Tıklama ve zıplama bekleme sürelerini kaldırır.", Category.PLAYER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        // We set the click delay to 0 to allow continuous clicking
        ((MinecraftAccessor) mc).setLeftClickCounter(0);
        // Also remove jump delay if not already done by NoJumpDelay
        ((EntityLivingBaseAccessor) mc.thePlayer).setJumpTicks(0);
    }
}
