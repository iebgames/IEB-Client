package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import dev.iebgames.iebclient.setting.NumberSetting;
import dev.iebgames.iebclient.mixin.MinecraftAccessor;
import dev.iebgames.iebclient.IEBClient;
import org.lwjgl.input.Keyboard;

public class TickBase extends Module {

    private final NumberSetting range = register(new NumberSetting("Range", 3.0, 1.0, 6.0, 0.1));

    public TickBase() {
        super("TickBase", "Zamanı bükerek ekstra vuruş şansı sağlar.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        // Basic tickbase logic: if target is close, slightly speed up the game timer for the player
        // Note: Real tickbase is much more complex, this is a simplified version.
        KillAura aura = IEBClient.moduleManager.getModule(KillAura.class);
        if (aura != null && aura.isEnabled() && aura.getTarget() != null) {
            ((MinecraftAccessor) mc).getTimer().timerSpeed = 1.1f;
        } else {
            ((MinecraftAccessor) mc).getTimer().timerSpeed = 1.0f;
        }
    }

    @Override
    protected void onDisable() {
        ((MinecraftAccessor) mc).getTimer().timerSpeed = 1.0f;
    }
}
