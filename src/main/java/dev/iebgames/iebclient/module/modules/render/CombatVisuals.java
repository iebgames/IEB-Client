package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventAttack;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.util.EnumParticleTypes;
import org.lwjgl.input.Keyboard;

public class CombatVisuals extends Module {

    public CombatVisuals() {
        super("CombatVisuals", "Savaş görsel efektleri ekler.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onAttack(EventAttack e) {
        if (mc.theWorld == null || e.getTarget() == null) return;
        for (int i = 0; i < 5; i++) {
            mc.theWorld.spawnParticle(EnumParticleTypes.CRIT,
                    e.getTarget().posX, e.getTarget().posY + 1, e.getTarget().posZ,
                    0, 0, 0);
        }
    }
}
