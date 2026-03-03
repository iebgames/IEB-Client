package dev.iebgames.iebclient.gui;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HUD {
    private final Minecraft mc = Minecraft.getMinecraft();

    public void draw() {
        ScaledResolution sr = new ScaledResolution(mc);
        FontRenderer fr = mc.fontRendererObj;

        // Watermark
        fr.drawStringWithShadow("IEB Client v2.0", 4, 4, 0xFFFF0000);

        // Modules List
        List<Module> enabledModules = IEBClient.INSTANCE.moduleManager.getModules().stream()
                .filter(Module::isEnabled)
                .sorted(Comparator.comparingInt(m -> -fr.getStringWidth(m.getName())))
                .collect(Collectors.toList());

        int y = 4;
        for (Module m : enabledModules) {
            fr.drawStringWithShadow(m.getName(), sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 4, y, 0xFFFF0000);
            y += fr.FONT_HEIGHT + 2;
        }
    }
}
