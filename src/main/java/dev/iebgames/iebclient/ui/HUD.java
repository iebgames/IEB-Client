package dev.iebgames.iebclient.ui;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.module.modules.render.BlockOverlay;
import dev.iebgames.iebclient.module.modules.render.ClickGUIModule;
import dev.iebgames.iebclient.module.modules.render.Crosshair;
import dev.iebgames.iebclient.module.modules.render.TargetHUD;
import dev.iebgames.iebclient.util.ColorUtils;
import dev.iebgames.iebclient.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import java.util.List;

public class HUD extends Gui {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

    public void draw() {
        // Draw Watermark
        fr.drawStringWithShadow(IEBClient.NAME + " v" + IEBClient.VERSION, 4, 4, 0xFFFF0000);

        // Draw Active Modules List
        int y = 14;
        List<Module> modules = IEBClient.moduleManager.getModules();
        
        // Sort by length or just list them
        for (Module m : modules) {
            if (m.isToggled()) {
                fr.drawStringWithShadow(m.getName(), 4, y, -1);
                y += 10;
            }
        }

        // Draw Copyright
        ScaledResolution sr = new ScaledResolution(mc);
        String copyright = "© IEB Games";
        fr.drawStringWithShadow(copyright, sr.getScaledWidth() - fr.getStringWidth(copyright) - 4, sr.getScaledHeight() - 12, 0x88FFFFFF);

        // Draw TargetHUD
        TargetHUD targetHUD = IEBClient.moduleManager.getModule(TargetHUD.class);
        if (targetHUD != null && targetHUD.isToggled()) {
            targetHUD.render(sr);
        }

        // Draw Crosshair
        Crosshair crosshair = IEBClient.moduleManager.getModule(Crosshair.class);
        if (crosshair != null && crosshair.isToggled()) {
            drawCrosshair(sr, crosshair);
        }
    }

    private void drawCrosshair(ScaledResolution sr, Crosshair ch) {
        ClickGUIModule theme = IEBClient.moduleManager.getModule(ClickGUIModule.class);
        int x = sr.getScaledWidth() / 2;
        int y = sr.getScaledHeight() / 2;
        int color = theme.crosshairColor.isChroma() ? ColorUtils.getChroma(3000, 0) : theme.crosshairColor.getColor();
        float size = ch.size.getFloat();
        float gap = ch.gap.getFloat();

        // Horizontal lines
        RenderUtils.draw2DRect(x - (int)(gap + size), y - 1, (int)size, 2, color);
        RenderUtils.draw2DRect(x + (int)gap, y - 1, (int)size, 2, color);
        
        // Vertical lines
        RenderUtils.draw2DRect(x - 1, y - (int)(gap + size), 2, (int)size, color);
        RenderUtils.draw2DRect(x - 1, y + (int)gap, 2, (int)size, color);
    }
}
