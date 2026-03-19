package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class ShieldBreaker extends Module {

    public ShieldBreaker() {
        super("ShieldBreaker", "Kalkanı olan oyunculara axe ile vurur (1.8.9'da kalkan yoktur, placeholder).", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        // 1.8.9 placeholder. Logic would involve detecting 1.9 shields if using a protocol support or similar.
    }
}
