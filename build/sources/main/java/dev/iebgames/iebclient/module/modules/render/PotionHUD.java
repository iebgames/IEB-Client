package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventRender2D;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;

public class PotionHUD extends Module {

    public PotionHUD() {
        super("PotionHUD", "Aktif iksir sürelerini gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onRender2D(EventRender2D e) {
        if (mc.thePlayer == null) return;
        int y = 20;
        for (PotionEffect effect : mc.thePlayer.getActivePotionEffects()) {
            String name = effect.getEffectName();
            int sec = effect.getDuration() / 20;
            String text = name + " " + sec + "s";
            mc.fontRendererObj.drawStringWithShadow(text, mc.displayWidth / 2 / 2 + 80, y, 0xFFAAFFFF);
            y += 10;
        }
    }
}
