package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.module.modules.combat.KillAura;
import dev.iebgames.iebclient.util.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

public class TargetHUD extends Module {

    public TargetHUD() {
        super("TargetHUD", "Saldırdığın kişinin durumunu ekranda gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        // Actual rendering happens in a GUI layer, this just triggers or holds state
    }

    public void render(ScaledResolution sr) {
        KillAura aura = IEBClient.INSTANCE.moduleManager.getModule(KillAura.class);
        if (aura == null || !aura.isEnabled()) return;

        EntityLivingBase target = aura.getTarget();
        if (target == null) return;

        int x = sr.getScaledWidth() / 2 + 10;
        int y = sr.getScaledHeight() / 2 + 10;
        
        RenderUtils.draw2DRect(x, y, 120, 40, 0xBB000000);
        mc.fontRendererObj.drawStringWithShadow(target.getName(), x + 5, y + 5, -1);
        
        float hpPerc = target.getHealth() / target.getMaxHealth();
        RenderUtils.draw2DRect(x + 5, y + 20, 110, 5, 0x55FF0000);
        RenderUtils.draw2DRect(x + 5, y + 20, (int)(110 * hpPerc), 5, 0xFFFF0000);
        mc.fontRendererObj.drawString( (int)target.getHealth() + " HP", x + 5, y + 28, -1);
    }
}
