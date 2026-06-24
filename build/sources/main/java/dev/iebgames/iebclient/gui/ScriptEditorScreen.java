package dev.iebgames.iebclient.gui;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.modules.render.ClickGUIModule;
import dev.iebgames.iebclient.util.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScriptEditorScreen extends GuiScreen {

    private final String fileName;
    private final GuiScreen parent;
    private final List<String> lines = new ArrayList<>();
    private int cursorX;
    private int cursorY;
    private int scrollLine;
    private int visibleLines;
    private String statusMessage = "";
    private long statusUntil;
    private boolean modified;

    public ScriptEditorScreen(String fileName, GuiScreen parent) {
        this.fileName = fileName;
        this.parent = parent;
        String content = IEBClient.scriptManager.getScriptContent(fileName);
        if (content.isEmpty()) {
            lines.add("");
        } else {
            for (String line : content.split("\n", -1)) {
                lines.add(line);
            }
        }
        cursorX = 0;
        cursorY = 0;
        scrollLine = 0;
        modified = false;
    }

    @Override
    public void initGui() {
        visibleLines = Math.max(10, (height - 80) / 12);
    }

    private void setStatus(String msg) {
        statusMessage = msg;
        statusUntil = System.currentTimeMillis() + 3000;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        ClickGUIModule theme = IEBClient.moduleManager.getModule(ClickGUIModule.class);
        int accent = theme.accentColor.getColor();

        RenderUtils.drawBorderedRect(10, 10, width - 20, height - 20, 0xEE111111, 1, accent);

        mc.fontRendererObj.drawStringWithShadow("Script Editor — " + fileName, 20, 18, accent);
        mc.fontRendererObj.drawStringWithShadow("Ctrl+S Save | Ctrl+R Reload | F5 Test Toggle | ESC Back", 20, 30, 0xAAFFFFFF);

        int editorTop = 45;
        int editorBottom = height - 35;
        int editorHeight = editorBottom - editorTop;
        visibleLines = Math.max(10, editorHeight / 12);

        RenderUtils.draw2DRect(20, editorTop, width - 40, editorHeight, 0xFF0A0A0A);

        int lineNumWidth = 36;
        for (int i = 0; i < visibleLines; i++) {
            int lineIndex = scrollLine + i;
            if (lineIndex >= lines.size()) break;

            int y = editorTop + 4 + i * 12;
            int lineColor = (lineIndex == cursorY) ? accent : 0x66FFFFFF;
            String num = String.valueOf(lineIndex + 1);
            mc.fontRendererObj.drawString(num, 24, y, lineColor);

            String line = lines.get(lineIndex);
            mc.fontRendererObj.drawString(line, 20 + lineNumWidth, y, -1);

            if (lineIndex == cursorY) {
                String before = line.substring(0, Math.min(cursorX, line.length()));
                int cx = 20 + lineNumWidth + mc.fontRendererObj.getStringWidth(before);
                if (System.currentTimeMillis() / 500 % 2 == 0) {
                    RenderUtils.draw2DRect(cx, y, 1, 10, 0xFFFFFFFF);
                }
            }
        }

        String error = IEBClient.scriptManager.getLastError(fileName);
        int footerY = height - 28;
        String footer = modified ? "● Modified" : "Saved";
        if (error != null) footer = "§cError: " + error;
        mc.fontRendererObj.drawStringWithShadow(footer, 20, footerY, error != null ? 0xFFFF5555 : 0xFF55FF55);

        if (System.currentTimeMillis() < statusUntil && !statusMessage.isEmpty()) {
            mc.fontRendererObj.drawStringWithShadow(statusMessage, width / 2 - mc.fontRendererObj.getStringWidth(statusMessage) / 2, footerY, accent);
        }

        RenderUtils.drawBorderedRect(width - 170, 14, 70, 14, 0xBB000000, 1, 0xFF55FF55);
        mc.fontRendererObj.drawStringWithShadow("SAVE", width - 155, 17, 0xFF55FF55);
        RenderUtils.drawBorderedRect(width - 90, 14, 70, 14, 0xBB000000, 1, 0xFFFF5555);
        mc.fontRendererObj.drawStringWithShadow("CLOSE", width - 75, 17, 0xFFFF5555);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseX >= width - 170 && mouseX <= width - 100 && mouseY >= 14 && mouseY <= 28) {
            save();
            return;
        }
        if (mouseX >= width - 90 && mouseX <= width - 20 && mouseY >= 14 && mouseY <= 28) {
            mc.displayGuiScreen(parent);
            return;
        }

        int editorTop = 45;
        int lineNumWidth = 36;
        if (mouseX >= 20 + lineNumWidth && mouseX <= width - 20 && mouseY >= editorTop && mouseY <= height - 35) {
            cursorY = scrollLine + (mouseY - editorTop - 4) / 12;
            cursorY = Math.max(0, Math.min(cursorY, lines.size() - 1));
            String line = lines.get(cursorY);
            int relX = mouseX - (20 + lineNumWidth);
            cursorX = 0;
            for (int i = 0; i <= line.length(); i++) {
                if (mc.fontRendererObj.getStringWidth(line.substring(0, i)) > relX) break;
                cursorX = i;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            if (wheel > 0) scrollLine = Math.max(0, scrollLine - 3);
            else scrollLine = Math.min(Math.max(0, lines.size() - visibleLines), scrollLine + 3);
        }
        super.handleMouseInput();
    }

    private void save() {
        String content = String.join("\n", lines);
        IEBClient.scriptManager.saveScript(fileName, content);
        modified = false;
        setStatus("Saved & reloaded!");
    }

    private void ensureLine() {
        while (lines.size() <= cursorY) lines.add("");
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            if (modified) {
                save();
            }
            mc.displayGuiScreen(parent);
            return;
        }

        if (keyCode == Keyboard.KEY_S && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            save();
            return;
        }

        if (keyCode == Keyboard.KEY_R && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            save();
            IEBClient.scriptManager.loadScripts();
            setStatus("Reloaded all scripts");
            return;
        }

        if (keyCode == Keyboard.KEY_F5) {
            dev.iebgames.iebclient.script.ScriptModule mod = IEBClient.scriptManager.getModuleForFile(fileName);
            if (mod != null) {
                mod.toggle();
                setStatus(mod.isEnabled() ? "Script module enabled" : "Script module disabled");
            } else {
                setStatus("Save script first to test");
            }
            return;
        }

        ensureLine();
        String line = lines.get(cursorY);

        if (keyCode == Keyboard.KEY_RETURN) {
            String left = line.substring(0, cursorX);
            String right = line.substring(cursorX);
            lines.set(cursorY, left);
            lines.add(cursorY + 1, right);
            cursorY++;
            cursorX = 0;
            modified = true;
            return;
        }

        if (keyCode == Keyboard.KEY_BACK) {
            if (cursorX > 0) {
                lines.set(cursorY, line.substring(0, cursorX - 1) + line.substring(cursorX));
                cursorX--;
            } else if (cursorY > 0) {
                String prev = lines.get(cursorY - 1);
                cursorX = prev.length();
                lines.set(cursorY - 1, prev + line);
                lines.remove(cursorY);
                cursorY--;
            }
            modified = true;
            return;
        }

        if (keyCode == Keyboard.KEY_DELETE) {
            if (cursorX < line.length()) {
                lines.set(cursorY, line.substring(0, cursorX) + line.substring(cursorX + 1));
            } else if (cursorY < lines.size() - 1) {
                lines.set(cursorY, line + lines.get(cursorY + 1));
                lines.remove(cursorY + 1);
            }
            modified = true;
            return;
        }

        if (keyCode == Keyboard.KEY_LEFT) {
            if (cursorX > 0) cursorX--;
            else if (cursorY > 0) {
                cursorY--;
                cursorX = lines.get(cursorY).length();
            }
            return;
        }

        if (keyCode == Keyboard.KEY_RIGHT) {
            if (cursorX < line.length()) cursorX++;
            else if (cursorY < lines.size() - 1) {
                cursorY++;
                cursorX = 0;
            }
            return;
        }

        if (keyCode == Keyboard.KEY_UP) {
            if (cursorY > 0) {
                cursorY--;
                cursorX = Math.min(cursorX, lines.get(cursorY).length());
                if (cursorY < scrollLine) scrollLine = cursorY;
            }
            return;
        }

        if (keyCode == Keyboard.KEY_DOWN) {
            if (cursorY < lines.size() - 1) {
                cursorY++;
                cursorX = Math.min(cursorX, lines.get(cursorY).length());
                if (cursorY >= scrollLine + visibleLines) scrollLine = cursorY - visibleLines + 1;
            }
            return;
        }

        if (keyCode == Keyboard.KEY_TAB) {
            String insert = "    ";
            lines.set(cursorY, line.substring(0, cursorX) + insert + line.substring(cursorX));
            cursorX += insert.length();
            modified = true;
            return;
        }

        if (typedChar >= 32 && typedChar != 127) {
            lines.set(cursorY, line.substring(0, cursorX) + typedChar + line.substring(cursorX));
            cursorX++;
            modified = true;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
