package dev.iebgames.iebclient.gui;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.module.modules.render.ClickGUIModule;
import dev.iebgames.iebclient.setting.*;
import dev.iebgames.iebclient.util.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickGUI extends GuiScreen {

    private final Map<Category, Integer> scrollOffsets = new HashMap<>();
    private final Map<Category, Integer> panelX = new HashMap<>();
    private final Map<Category, Integer> panelY = new HashMap<>();
    private final Map<Module, Boolean> expandedModules = new HashMap<>();
    private final Map<Setting<?>, Boolean> expandedSettings = new HashMap<>();
    private final int panelHeight = 250;
    private Module bindingModule = null;
    
    private Category draggingPanel = null;
    private int dragOffsetX = 0, dragOffsetY = 0;
    
    private String searchQuery = "";
    private boolean listeningSearch = false;

    private Setting<?> draggingSlider = null;
    private String draggingComponent = "";
    
    private NumberSetting editingSetting = null;
    private String editBuffer = "";
    
    // Script Management State
    private String addingScriptName = null;
    private String editingScriptName = null;
    private List<String> editorLines = new ArrayList<>();
    private int cursorX = 0, cursorY = 0;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        
        ClickGUIModule theme = IEBClient.moduleManager.getModule(ClickGUIModule.class);
        float scale = theme.scale.getFloat();
        int accent = theme.accentColor.getColor();
        int bg = theme.bgColor.getColor();

        // Transform mouse coordinates
        int scaledMouseX = (int)(mouseX / scale);
        int scaledMouseY = (int)(mouseY / scale);

        net.minecraft.client.renderer.GlStateManager.pushMatrix();
        net.minecraft.client.renderer.GlStateManager.scale(scale, scale, 1.0f);
        
        // Search Bar
        RenderUtils.drawBorderedRect(20, 2, 120, 14, 0xBB000000, 1, listeningSearch ? accent : -1);
        String sText = searchQuery.isEmpty() ? (listeningSearch ? "" : "Search...") : searchQuery + (listeningSearch && (System.currentTimeMillis() / 500 % 2 == 0) ? "_" : "");
        mc.fontRendererObj.drawString(sText, 25, 5, 0xAAFFFFFF);

        // Reset Button
        RenderUtils.drawBorderedRect(150, 2, 60, 14, 0xBB000000, 1, 0xFFFF5555);
        mc.fontRendererObj.drawStringWithShadow("RESET ALL", 155, 5, 0xFFFF5555);

        // Reload Scripts Button
        RenderUtils.drawBorderedRect(220, 2, 90, 14, 0xBB000000, 1, 0xFF55FF55);
        mc.fontRendererObj.drawStringWithShadow("RELOAD SCRIPTS", 225, 5, 0xFF55FF55);

        // Update dragging
        if (draggingPanel != null) {
            panelX.put(draggingPanel, scaledMouseX - dragOffsetX);
            panelY.put(draggingPanel, scaledMouseY - dragOffsetY);
        }

        for (Category category : Category.values()) {
            int x = panelX.getOrDefault(category, 20 + category.ordinal() * 110);
            int y = panelY.getOrDefault(category, 20);
            int offset = scrollOffsets.getOrDefault(category, 0);
            
            // Panel Header
            RenderUtils.drawBorderedRect(x, y, 100, 15, 0xBB000000, 1, accent);
            mc.fontRendererObj.drawStringWithShadow(category.name(), x + 5, y + 3, -1);
            
            int yStart = y + 17;
            int yPos = yStart - offset;
            
            int totalHeight = calculateCategoryHeight(category);
            if (category == Category.SCRIPTS) {
                // ADD SCRIPT button
                int btnY = yPos;
                if (btnY >= yStart - 14 && btnY < yStart + panelHeight) {
                    RenderUtils.drawBorderedRect(x, btnY, 100, 14, 0xBB000000, 1, 0xFF55FF55);
                    mc.fontRendererObj.drawString("+ ADD SCRIPT", x + 15, btnY + 3, 0xFF55FF55);
                }
                yPos += 16;
                
                File[] files = IEBClient.INSTANCE.scriptManager.getScriptFiles();
                if (files != null) {
                    for (File f : files) {
                        if (yPos >= yStart - 14 && yPos < yStart + panelHeight) {
                            RenderUtils.draw2DRect(x, yPos, 100, 14, bg);
                            mc.fontRendererObj.drawString(f.getName(), x + 5, yPos + 3, -1);
                            
                            // Edit/Delete buttons
                            RenderUtils.draw2DRect(x + 70, yPos + 2, 12, 10, 0xAA00AAFF);
                            mc.fontRendererObj.drawString("E", x + 74, yPos + 2, -1);
                            RenderUtils.draw2DRect(x + 85, yPos + 2, 12, 10, 0xAAFF0000);
                            mc.fontRendererObj.drawString("D", x + 89, yPos + 2, -1);
                        }
                        yPos += 15;
                    }
                }
            } else {
                List<Module> modules = IEBClient.INSTANCE.moduleManager.getModulesByCategory(category);
                for (Module m : modules) {
                    // ... existing module rendering ...
                    if (!searchQuery.isEmpty() && !m.getName().toLowerCase().contains(searchQuery.toLowerCase())) continue;

                    // Module Row
                    if (yPos >= yStart - 14 && yPos < yStart + panelHeight) {
                        int color = m.isEnabled() ? accent : -1;
                        String name = m == bindingModule ? "[BINDING...]" : m.getName();
                        
                        RenderUtils.draw2DRect(x, yPos, 100, 14, bg);
                        mc.fontRendererObj.drawStringWithShadow(name, x + 5, yPos + 3, color);
                        
                        // ... bind/delete buttons ...
                        if (m == bindingModule) {
                            RenderUtils.draw2DRect(x + 70, yPos + 2, 28, 10, 0xAAFF0000);
                            mc.fontRendererObj.drawString("DELETE", x + 72, yPos + 2, -1);
                        } else if (m.getKeybind() != 0) {
                            String kb = "[" + Keyboard.getKeyName(m.getKeybind()) + "]";
                            mc.fontRendererObj.drawStringWithShadow(kb, x + 100 - mc.fontRendererObj.getStringWidth(kb) - 2, yPos + 3, 0x99FFFFFF);
                        }
                    }
                    yPos += 15;

                    // Settings
                    if (expandedModules.getOrDefault(m, false)) {
                        for (Setting<?> s : m.getSettings()) {
                            if (yPos >= yStart - 14 && yPos < yStart + panelHeight) {
                                RenderUtils.draw2DRect(x, yPos, 100, 12, 0x55000000);
                                String sLabel = s.getName() + ": " + s.getValue();
                                if (s instanceof ColorSetting) {
                                    sLabel = s.getName();
                                    RenderUtils.draw2DRect(x + 85, yPos + 2, 8, 8, ((ColorSetting) s).getColor());
                                }
                                if (s == editingSetting) sLabel = "> " + s.getName() + ": (editing)";
                                mc.fontRendererObj.drawString(sLabel, x + 10, yPos + 2, 0xCCFFFFFF);
                            }
                            yPos += 13;
                            // ... color picker sliders ...
                            if (s instanceof ColorSetting && expandedSettings.getOrDefault(s, false)) {
                                ColorSetting cs = (ColorSetting) s;
                                String[] comps = {"R", "G", "B", "A"};
                                int[] vals = {cs.getRed(), cs.getGreen(), cs.getBlue(), cs.getAlpha()};
                                for (int i = 0; i < 4; i++) {
                                    if (yPos >= yStart - 14 && yPos < yStart + panelHeight) {
                                        RenderUtils.draw2DRect(x, yPos, 100, 11, 0x77000000);
                                        float perc = vals[i] / 255f;
                                        RenderUtils.draw2DRect(x + 10, yPos + 4, 80, 2, 0x44FFFFFF);
                                        RenderUtils.draw2DRect(x + 10 + (int)(perc * 78), yPos + 2, 2, 6, accent);
                                        mc.fontRendererObj.drawString(comps[i], x + 2, yPos + 1, -1);
                                        if (draggingSlider == s && draggingComponent.equals(comps[i])) {
                                            float newPerc = Math.max(0, Math.min(1, (float)(scaledMouseX - (x + 10)) / 80));
                                            int newVal = (int)(newPerc * 255);
                                            if (i == 0) cs.setRed(newVal);
                                            else if (i == 1) cs.setGreen(newVal);
                                            else if (i == 2) cs.setBlue(newVal);
                                            else if (i == 3) cs.setAlpha(newVal);
                                        }
                                    }
                                    yPos += 11;
                                }
                                if (yPos >= yStart - 14 && yPos < yStart + panelHeight) {
                                    RenderUtils.draw2DRect(x, yPos, 100, 12, 0x77000000);
                                    mc.fontRendererObj.drawString("Chroma", x + 10, yPos + 2, -1);
                                    RenderUtils.drawBorderedRect(x + 80, yPos + 2, 8, 8, 0x99000000, 1, cs.isChroma() ? accent : -1);
                                    if (cs.isChroma()) RenderUtils.draw2DRect(x + 82, yPos + 4, 4, 4, accent);
                                }
                                yPos += 13;
                            }
                        }
                    }
                }
            }
            // Scrollbar
            totalHeight = calculateCategoryHeight(category);
            if (totalHeight > panelHeight) {
                RenderUtils.draw2DRect(x + 98, yStart, 2, panelHeight, 0x33FFFFFF);
                float scrollPercent = (float) offset / (totalHeight - panelHeight);
                int barY = (int) (yStart + scrollPercent * (panelHeight - 20));
                RenderUtils.draw2DRect(x + 98, barY, 2, 20, accent);
            }
            x += 110;
        }

        net.minecraft.client.renderer.GlStateManager.popMatrix();

        // Copyright info
        String copyright = "© IEB Games - Dev Team 2026";
        mc.fontRendererObj.drawStringWithShadow(copyright, width - mc.fontRendererObj.getStringWidth(copyright) - 5, height - 12, 0x77FFFFFF);
        
        super.drawScreen(mouseX, mouseY, partialTicks);
        
        // Edit Overlay (if editing a number)
        if (editingSetting != null) {
            String editMsg = "Editing " + editingSetting.getName() + ": " + editBuffer + "_";
            int mw = mc.fontRendererObj.getStringWidth(editMsg);
            RenderUtils.drawBorderedRect(width / 2 - (mw/2 + 10), height / 2 - 20, mw + 20, 30, 0xEE000000, 1, accent);
            mc.fontRendererObj.drawStringWithShadow(editMsg, width / 2 - mw/2, height / 2 - 10, -1);
            mc.fontRendererObj.drawStringWithShadow("Press ENTER to save, ESC to cancel", width / 2 - 80, height / 2 + 2, 0xAAFFFFFF);
        }
        
        // Add Script Overlay
        if (addingScriptName != null) {
            String msg = "New Script Name: " + addingScriptName + "_";
            int mw = mc.fontRendererObj.getStringWidth(msg);
            RenderUtils.drawBorderedRect(width / 2 - (mw/2 + 10), height / 2 - 20, mw + 20, 30, 0xEE000000, 1, 0xFF55FF55);
            mc.fontRendererObj.drawStringWithShadow(msg, width / 2 - mw/2, height / 2 - 10, -1);
            mc.fontRendererObj.drawStringWithShadow("Press ENTER to create, ESC to cancel", width / 2 - 80, height / 2 + 2, 0xAAFFFFFF);
        }
        
        // Script Editor Overlay
        if (editingScriptName != null) {
            RenderUtils.draw2DRect(0, 0, width, height, 0xDD000000);
            RenderUtils.drawBorderedRect(20, 20, width - 40, height - 40, 0xEE111111, 1, accent);
            mc.fontRendererObj.drawStringWithShadow("Editing: " + editingScriptName, 30, 30, accent);
            mc.fontRendererObj.drawStringWithShadow("ESC: Back | CTRL+S: Save | ENTER: New Line", width - 210, 30, 0xAAFFFFFF);
            
            int yE = 50;
            for (int i = 0; i < editorLines.size(); i++) {
                String line = editorLines.get(i);
                mc.fontRendererObj.drawString(line, 40, yE, -1);
                if (i == cursorY) {
                    int cw = mc.fontRendererObj.getStringWidth(line.substring(0, Math.min(line.length(), cursorX)));
                    RenderUtils.draw2DRect(40 + cw, yE, 1, 9, 0xFFFFFFFF); // Cursor
                }
                yE += 10;
            }
        }
    }

    private int calculateCategoryHeight(Category cat) {
        if (cat == Category.SCRIPTS) {
            File[] files = IEBClient.INSTANCE.scriptManager.getScriptFiles();
            return 16 + (files == null ? 0 : files.length * 15);
        }
        int h = 0;
        for (Module m : IEBClient.INSTANCE.moduleManager.getModulesByCategory(cat)) {
            if (!searchQuery.isEmpty() && !m.getName().toLowerCase().contains(searchQuery.toLowerCase())) continue;
            h += 15;
            if (expandedModules.getOrDefault(m, false)) {
                for (Setting<?> s : m.getSettings()) {
                    h += 13;
                    if (s instanceof ColorSetting && expandedSettings.getOrDefault(s, false)) {
                        h += (11 * 4) + 13; // Sliders + Chroma
                    }
                }
            }
        }
        return h;
    }

    @Override
    public void handleMouseInput() throws IOException {
        NumberSetting scaleSetting = IEBClient.moduleManager.getModule(ClickGUIModule.class).scale;
        float scale = scaleSetting.getFloat();
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                double current = scaleSetting.getValue();
                if (wheel > 0) scaleSetting.setValue(Math.min(1.0, current + 0.05));
                else scaleSetting.setValue(Math.max(0.5, current - 0.05));
                return;
            }
            int mouseX = (int)((Mouse.getEventX() * width / mc.displayWidth) / scale);
            int mouseY = (int)((height - Mouse.getEventY() * height / mc.displayHeight - 1) / scale);
            for (Category category : Category.values()) {
                int x = panelX.getOrDefault(category, 20 + category.ordinal() * 110);
                int y = panelY.getOrDefault(category, 20);
                if (mouseX >= x && mouseX <= x + 100 && mouseY >= y && mouseY <= y + panelHeight + 15) {
                    int offset = scrollOffsets.getOrDefault(category, 0);
                    int maxScroll = Math.max(0, calculateCategoryHeight(category) - panelHeight);
                    if (wheel > 0) offset -= 30;
                    else offset += 30;
                    scrollOffsets.put(category, Math.max(0, Math.min(offset, maxScroll)));
                }
            }
        }
        super.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        float scale = IEBClient.moduleManager.getModule(ClickGUIModule.class).scale.getFloat();
        mouseX /= scale;
        mouseY /= scale;

        // Search Bar click
        if (mouseX >= 20 && mouseX <= 140 && mouseY >= 2 && mouseY <= 16) {
            listeningSearch = true;
            return;
        } else {
            listeningSearch = false;
        }

        // Reset Button click
        if (mouseX >= 150 && mouseX <= 210 && mouseY >= 2 && mouseY <= 16) {
            IEBClient.INSTANCE.moduleManager.resetAll();
            panelX.clear();
            panelY.clear();
            return;
        }

        // Reload Scripts Button click
        if (mouseX >= 220 && mouseX <= 310 && mouseY >= 2 && mouseY <= 16) {
            IEBClient.INSTANCE.scriptManager.loadScripts();
            return;
        }

        for (Category category : Category.values()) {
            int x = panelX.getOrDefault(category, 20 + category.ordinal() * 110);
            int y = panelY.getOrDefault(category, 20);
            
            // Drag detection
            if (mouseX >= x && mouseX <= x + 100 && mouseY >= y && mouseY <= y + 15) {
                draggingPanel = category;
                dragOffsetX = mouseX - x;
                dragOffsetY = mouseY - y;
                return;
            }

            int offset = scrollOffsets.getOrDefault(category, 0);
            int yStart = y + 17;
            int yPos = yStart - offset;
            
            if (category == Category.SCRIPTS) {
                // ADD SCRIPT click
                if (mouseX >= x && mouseX <= x + 100 && mouseY >= yPos && mouseY <= yPos + 14 && mouseY >= yStart && mouseY < yStart + panelHeight) {
                    addingScriptName = "";
                    return;
                }
                yPos += 16;
                
                File[] files = IEBClient.INSTANCE.scriptManager.getScriptFiles();
                if (files != null) {
                    for (File f : files) {
                        if (mouseX >= x && mouseX <= x + 100 && mouseY >= yPos && mouseY <= yPos + 14 && mouseY >= yStart && mouseY < yStart + panelHeight) {
                            if (mouseX >= x + 70 && mouseX <= x + 82) { // EDIT
                                editingScriptName = f.getName();
                                String content = IEBClient.INSTANCE.scriptManager.getScriptContent(f.getName());
                                editorLines = new ArrayList<>(java.util.Arrays.asList(content.split("\n", -1)));
                                cursorX = 0; cursorY = 0;
                                return;
                            } else if (mouseX >= x + 85 && mouseX <= x + 97) { // DELETE
                                IEBClient.INSTANCE.scriptManager.deleteScript(f.getName());
                                return;
                            }
                        }
                        yPos += 15;
                    }
                }
            } else {
                for (Module m : IEBClient.INSTANCE.moduleManager.getModulesByCategory(category)) {
                    // ... existing module clicks ...
                    if (!searchQuery.isEmpty() && !m.getName().toLowerCase().contains(searchQuery.toLowerCase())) continue;

                    if (mouseX >= x && mouseX <= x + 100 && mouseY >= yPos && mouseY <= yPos + 14 && mouseY >= yStart && mouseY < yStart + panelHeight) {
                        if (m == bindingModule && mouseX >= x + 70 && mouseX <= x + 98) {
                            m.setKeybind(0);
                            bindingModule = null;
                            return;
                        }
                        if (mouseButton == 0) m.toggle();
                        else if (mouseButton == 1) bindingModule = m;
                        else if (mouseButton == 2) expandedModules.put(m, !expandedModules.getOrDefault(m, false));
                        return;
                    }
                    yPos += 15;

                    if (expandedModules.getOrDefault(m, false)) {
                        for (Setting<?> s : m.getSettings()) {
                            if (mouseX >= x && mouseX <= x + 100 && mouseY >= yPos && mouseY <= yPos + 12 && mouseY >= yStart && mouseY < yStart + panelHeight) {
                                if (mouseButton == 0) {
                                    if (s instanceof BooleanSetting) ((BooleanSetting) s).toggle();
                                    if (s instanceof ColorSetting) expandedSettings.put(s, !expandedSettings.getOrDefault(s, false));
                                } else if (mouseButton == 2 && s instanceof NumberSetting) {
                                    editingSetting = (NumberSetting) s;
                                    editBuffer = s.getValue().toString();
                                    return;
                                }
                                return;
                            }
                            yPos += 13;

                            if (s instanceof ColorSetting && expandedSettings.getOrDefault(s, false)) {
                                String[] comps = {"R", "G", "B", "A"};
                                for (int i = 0; i < 4; i++) {
                                    if (mouseX >= x + 10 && mouseX <= x + 90 && mouseY >= yPos && mouseY <= yPos + 11 && mouseY >= yStart && mouseY < yStart + panelHeight) {
                                        draggingSlider = s;
                                        draggingComponent = comps[i];
                                        return;
                                    }
                                    yPos += 11;
                                }
                                if (mouseX >= x + 80 && mouseX <= x + 88 && mouseY >= yPos && mouseY <= yPos + 10 && mouseY >= yStart && mouseY < yStart + panelHeight) {
                                    ((ColorSetting) s).setChroma(!((ColorSetting) s).isChroma());
                                    return;
                                }
                                yPos += 13;
                            }
                        }
                    }
                }
            }
            x += 110;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        float scale = IEBClient.moduleManager.getModule(ClickGUIModule.class).scale.getFloat();
        mouseX /= scale;
        mouseY /= scale;

        draggingSlider = null;
        draggingComponent = "";
        draggingPanel = null;
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (listeningSearch) {
            if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RETURN) {
                listeningSearch = false;
            } else if (keyCode == Keyboard.KEY_BACK) {
                if (!searchQuery.isEmpty()) searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
            } else if (Character.isLetterOrDigit(typedChar) || typedChar == ' ') {
                searchQuery += typedChar;
            }
            return;
        }

        if (editingSetting != null) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                editingSetting = null;
            } else if (keyCode == Keyboard.KEY_RETURN) {
                try {
                    double val = Double.parseDouble(editBuffer);
                    editingSetting.setValue(val);
                } catch (Exception ignored) {}
                editingSetting = null;
            } else if (keyCode == Keyboard.KEY_BACK) {
                if (!editBuffer.isEmpty()) editBuffer = editBuffer.substring(0, editBuffer.length() - 1);
            } else if (Character.isDigit(typedChar) || typedChar == '.' || typedChar == '-') {
                editBuffer += typedChar;
            }
            return;
        }

        if (addingScriptName != null) {
            if (keyCode == Keyboard.KEY_ESCAPE) addingScriptName = null;
            else if (keyCode == Keyboard.KEY_RETURN) {
                if (!addingScriptName.isEmpty()) IEBClient.INSTANCE.scriptManager.createScript(addingScriptName);
                addingScriptName = null;
            } else if (keyCode == Keyboard.KEY_BACK) {
                if (!addingScriptName.isEmpty()) addingScriptName = addingScriptName.substring(0, addingScriptName.length() - 1);
            } else if (Character.isLetterOrDigit(typedChar) || typedChar == '_' || typedChar == '-') {
                addingScriptName += typedChar;
            }
            return;
        }

        if (editingScriptName != null) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                editingScriptName = null;
            } else if (keyCode == Keyboard.KEY_S && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                String content = String.join("\n", editorLines);
                IEBClient.INSTANCE.scriptManager.saveScript(editingScriptName, content);
            } else if (keyCode == Keyboard.KEY_RETURN) {
                String currentLine = editorLines.get(cursorY);
                String left = currentLine.substring(0, cursorX);
                String right = currentLine.substring(cursorX);
                editorLines.set(cursorY, left);
                editorLines.add(cursorY + 1, right);
                cursorY++;
                cursorX = 0;
            } else if (keyCode == Keyboard.KEY_BACK) {
                String currentLine = editorLines.get(cursorY);
                if (cursorX > 0) {
                    editorLines.set(cursorY, currentLine.substring(0, cursorX - 1) + currentLine.substring(cursorX));
                    cursorX--;
                } else if (cursorY > 0) {
                    String prevLine = editorLines.get(cursorY - 1);
                    cursorX = prevLine.length();
                    editorLines.set(cursorY - 1, prevLine + currentLine);
                    editorLines.remove(cursorY);
                    cursorY--;
                }
            } else if (keyCode == Keyboard.KEY_LEFT) {
                if (cursorX > 0) cursorX--;
                else if (cursorY > 0) {
                    cursorY--;
                    cursorX = editorLines.get(cursorY).length();
                }
            } else if (keyCode == Keyboard.KEY_RIGHT) {
                if (cursorX < editorLines.get(cursorY).length()) cursorX++;
                else if (cursorY < editorLines.size() - 1) {
                    cursorY++;
                    cursorX = 0;
                }
            } else if (keyCode == Keyboard.KEY_UP) {
                if (cursorY > 0) {
                    cursorY--;
                    cursorX = Math.min(cursorX, editorLines.get(cursorY).length());
                }
            } else if (keyCode == Keyboard.KEY_DOWN) {
                if (cursorY < editorLines.size() - 1) {
                    cursorY++;
                    cursorX = Math.min(cursorX, editorLines.get(cursorY).length());
                }
            } else if (typedChar != 0 && typedChar >= 32 && typedChar <= 126) {
                String line = editorLines.get(cursorY);
                editorLines.set(cursorY, line.substring(0, cursorX) + typedChar + line.substring(cursorX));
                cursorX++;
            }
            return;
        }

        if (bindingModule != null) {
            if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_DELETE) bindingModule.setKeybind(0);
            else bindingModule.setKeybind(keyCode);
            bindingModule = null;
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }
}
