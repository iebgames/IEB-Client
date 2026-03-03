package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class AntiBot extends Module {

    private final List<EntityPlayer> bots = new ArrayList<>();

    public AntiBot() {
        super("AntiBot", "Bot oyuncuları tespit eder ve saldırıları engeller.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer) continue;
            // Basic check: bots often have invisible names or are in tab but not world
            if (player.getDisplayNameString().isEmpty() || player.isInvisible()) {
                if (!bots.contains(player)) bots.add(player);
            }
        }
    }

    public boolean isBot(EntityPlayer player) {
        return isEnabled() && bots.contains(player);
    }
}
