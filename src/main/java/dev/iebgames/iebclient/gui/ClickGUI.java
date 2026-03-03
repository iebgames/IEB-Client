package dev.iebgames.iebclient.gui;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.util.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class ClickGUI extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int x = 20;
        for (Category category : Category.values()) {
            RenderUtils.drawBorderedRect(x, 20, 100, 15, 0xBB000000, 1, 0xFFFF0000);
            mc.fontRendererObj.drawStringWithShadow(category.name(), x + 5, 23, -1);
            
            int y = 37;
            List<Module> modules = IEBClient.INSTANCE.moduleManager.getModulesByCategory(category);
            for (Module m : modules) {
                int color = m.isEnabled() ? 0xFFFF0000 : -1;
                RenderUtils.draw2DRect(x, y, 100, 14, 0x88000000);
                mc.fontRendererObj.drawStringWithShadow(m.getName(), x + 5, y + 3, color);
                y += 15;
            }
            x += 110;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int x = 20;
        for (Category category : Category.values()) {
            int y = 37;
            List<Module> modules = IEBClient.INSTANCE.moduleManager.getModulesByCategory(category);
            for (Module m : modules) {
                if (mouseX >= x && mouseX <= x + 100 && mouseY >= y && mouseY <= y + 14) {
                    m.toggle();
                }
                y += 15;
            }
            x += 110;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
