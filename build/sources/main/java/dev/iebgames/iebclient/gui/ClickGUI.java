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
    private TextSetting editingTextSetting = null;
    private String editBuffer = "";
    
    // Script Management State
    private String addingScriptName = null;
    private String editingScriptName = null;
    private List<String> editorLines = new ArrayList<>();
    private int cursorX = 0, cursorY = 0;
    
    // AI Sidebar State
    private boolean aiSidebarOpen = false;
    private final int sidebarWidth = 220;
    private String aiPrompt = "";
    private boolean listeningAIInput = false;
    private String selectedModel = "llama-3.3-70b-versatile";
    private dev.iebgames.iebclient.ai.AIManager.Provider aiProvider = dev.iebgames.iebclient.ai.AIManager.Provider.GROQ;
    private boolean modelDropdownOpen = false;
    private int modelScroll = 0;
    private int chatScroll = 0;
    private final dev.iebgames.iebclient.ai.AIManager aiManager = new dev.iebgames.iebclient.ai.AIManager();
    private String editingKeyType = null;
    private boolean showKeySelection = false;
    private String keyBuffer = "";

    private static class AIMessage {
        String text, code, name, raw, role;
        boolean showingDetails = false;
        boolean isUser;
        AIMessage(String text, boolean isUser) { 
            this.text = text; 
            this.isUser = isUser; 
            this.role = isUser ? "user" : "assistant";
        }
    }

    private static class Conversation {
        String title = "New Chat";
        java.util.List<AIMessage> messages = new java.util.ArrayList<>();
    }

    private final java.util.List<Conversation> aiConversations = new java.util.ArrayList<>();
    private Conversation currentConversation = null;
    private boolean showingChatHistory = false;

    private void saveAIHistory() {
        try {
            File file = new File(mc.mcDataDir, "ieb/ai_history.json");
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            String json = new com.google.gson.Gson().toJson(aiConversations);
            java.nio.file.Files.write(file.toPath(), json.getBytes());
        } catch (Exception ignored) {}
    }

    private void loadAIHistory() {
        try {
            File file = new File(mc.mcDataDir, "ieb/ai_history.json");
            if (file.exists()) {
                String json = new String(java.nio.file.Files.readAllBytes(file.toPath()));
                java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.List<Conversation>>(){}.getType();
                aiConversations.clear();
                aiConversations.addAll(new com.google.gson.Gson().fromJson(json, type));
            }
        } catch (Exception ignored) {}
        if (aiConversations.isEmpty()) {
            aiConversations.add(new Conversation());
        }
        if (currentConversation == null) currentConversation = aiConversations.get(0);
    }

    private Module hoveredInfoModule = null;

    private void drawModuleTooltip(int mouseX, int mouseY, String text) {
        List<String> lines = mc.fontRendererObj.listFormattedStringToWidth(text, 180);
        int h = lines.size() * 10 + 8;
        int w = 0;
        for (String line : lines) w = Math.max(w, mc.fontRendererObj.getStringWidth(line));
        w += 12;
        int tx = Math.min(mouseX + 12, width - w - 5);
        int ty = mouseY + 12;
        if (ty + h > height - 5) ty = mouseY - h - 5;
        RenderUtils.drawBorderedRect(tx, ty, w, h, 0xEE111111, 1, IEBClient.moduleManager.getModule(ClickGUIModule.class).accentColor.getColor());
        int ly = ty + 4;
        for (String line : lines) {
            mc.fontRendererObj.drawStringWithShadow(line, tx + 6, ly, 0xFFFFFFFF);
            ly += 10;
        }
    }

    private String censorKey(String key) {
        if (key == null || key.length() < 12) return "********";
        return key.substring(0, 6) + "..." + key.substring(key.length() - 4);
    }

    public ClickGUI() {
        loadAIHistory();
    }

    @Override
    public void initGui() {
        if (aiConversations.isEmpty()) loadAIHistory();
    }

    @Override
    public void onGuiClosed() {
        saveAIHistory();
        super.onGuiClosed();
    }

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

        float finalScale = scale;
        if (aiSidebarOpen) {
            float sidebarRatio = (float)sidebarWidth / width;
            finalScale *= (1.0f - sidebarRatio);
        }
        
        // Use separate scaled coordinates for PANELS
        int pMouseX = (int)(mouseX / finalScale);
        int pMouseY = (int)(mouseY / finalScale);

        net.minecraft.client.renderer.GlStateManager.pushMatrix();
        net.minecraft.client.renderer.GlStateManager.scale(finalScale, finalScale, 1.0f);
        
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
            panelX.put(draggingPanel, pMouseX - dragOffsetX);
            panelY.put(draggingPanel, pMouseY - dragOffsetY);
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
                // SCRIPT MAKER button
                int smY = yPos;
                if (smY >= yStart - 14 && smY < yStart + panelHeight) {
                    RenderUtils.drawBorderedRect(x, smY, 100, 14, 0xBB000000, 1, 0xFF55FFFF);
                    mc.fontRendererObj.drawString("✨ SCRIPT MAKER", x + 5, smY + 3, 0xFF55FFFF);
                }
                yPos += 16;
                
                // ADD SCRIPT button
                int btnY = yPos;
                if (btnY >= yStart - 14 && btnY < yStart + panelHeight) {
                    RenderUtils.drawBorderedRect(x, btnY, 100, 14, 0xBB000000, 1, 0xFF55FF55);
                    mc.fontRendererObj.drawString("+ ADD SCRIPT", x + 15, btnY + 3, 0xFF55FF55);
                }
                yPos += 16;
                
                File[] files = IEBClient.scriptManager.getScriptFiles();
                if (files != null) {
                    for (File f : files) {
                        if (yPos >= yStart - 14 && yPos < yStart + panelHeight) {
                            dev.iebgames.iebclient.script.ScriptModule m = IEBClient.scriptManager.getModuleForFile(f.getName());
                            int color = (m != null && m.isEnabled()) ? accent : -1;
                            String modName = m != null ? m.getName() : f.getName().replace(".js", "");
                            
                            RenderUtils.draw2DRect(x, yPos, 100, 14, bg);
                            mc.fontRendererObj.drawString(modName, x + 5, yPos + 3, color);
                            
                            // Error Indicator
                            String error = IEBClient.scriptManager.getLastError(f.getName());
                            if (error != null) {
                                mc.fontRendererObj.drawString("!", x + 48, yPos + 3, 0xFFFF5555);
                                if (pMouseX >= x + 46 && pMouseX <= x + 54 && pMouseY >= yPos && pMouseY <= yPos + 14) {
                                    RenderUtils.drawBorderedRect(pMouseX + 5, pMouseY + 5, Math.min(mc.fontRendererObj.getStringWidth(error) + 10, 180), 14, 0xEE000000, 1, 0xFFFF5555);
                                    mc.fontRendererObj.drawString(error, pMouseX + 10, pMouseY + 8, -1);
                                }
                            } else if (m != null) {
                                mc.fontRendererObj.drawString("OK", x + 48, yPos + 3, 0xFF55FF55);
                            }
                            
                            // Edit / Dup / Delete
                            RenderUtils.draw2DRect(x + 58, yPos + 2, 12, 10, 0xAA00AAFF);
                            mc.fontRendererObj.drawString("E", x + 62, yPos + 2, -1);
                            RenderUtils.draw2DRect(x + 72, yPos + 2, 12, 10, 0xAA55FF55);
                            mc.fontRendererObj.drawString("C", x + 76, yPos + 2, -1);
                            RenderUtils.draw2DRect(x + 86, yPos + 2, 12, 10, 0xAAFF0000);
                            mc.fontRendererObj.drawString("D", x + 90, yPos + 2, -1);
                        }
                        yPos += 15;
                    }
                }
            } else {
                List<Module> modules = IEBClient.moduleManager.getModulesByCategory(category);
                for (Module m : modules) {
                    // ... existing module rendering ...
                    if (!searchQuery.isEmpty() && !m.getName().toLowerCase().contains(searchQuery.toLowerCase())) continue;

                    // Module Row
                    if (yPos >= yStart - 14 && yPos < yStart + panelHeight) {
                        int color = m.isEnabled() ? accent : -1;
                        String name = m == bindingModule ? "[BINDING...]" : m.getName();
                        
                        RenderUtils.draw2DRect(x, yPos, 100, 14, bg);
                        mc.fontRendererObj.drawStringWithShadow(name, x + 5, yPos + 3, color);

                        RenderUtils.drawBorderedRect(x + 88, yPos + 2, 10, 10, 0x33000000, 1, 0x88FFFFFF);
                        mc.fontRendererObj.drawString("i", x + 91, yPos + 2, 0xFF55FFFF);
                        if (pMouseX >= x + 88 && pMouseX <= x + 98 && pMouseY >= yPos + 2 && pMouseY <= yPos + 12) {
                            hoveredInfoModule = m;
                        }
                        
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

        if (hoveredInfoModule != null) {
            drawModuleTooltip(mouseX, mouseY, hoveredInfoModule.getDescription());
        }
        hoveredInfoModule = null;

        if (aiSidebarOpen) {
            drawAISidebar(mouseX, mouseY);
        }

        // Copyright info
        String copyright = "© IEB Games - Dev Team 2026";
        mc.fontRendererObj.drawStringWithShadow(copyright, width - mc.fontRendererObj.getStringWidth(copyright) - 5, height - 12, 0x77FFFFFF);
        
        super.drawScreen(mouseX, mouseY, partialTicks);
        
        // Key Selection Overlay
        if (showKeySelection) {
            drawDefaultBackground();
            RenderUtils.drawBorderedRect(width / 2 - 100, height / 2 - 40, 200, 80, 0xEE000000, 1, accent);
            mc.fontRendererObj.drawStringWithShadow("Select Provider to Edit Key", width / 2 - 70, height / 2 - 30, -1);
            
            RenderUtils.drawBorderedRect(width / 2 - 80, height / 2 - 10, 160, 15, 0xBB000000, 1, 0xFF55FFFF);
            mc.fontRendererObj.drawString("Groq", width / 2 - 15, height / 2 - 6, -1);
            
            RenderUtils.drawBorderedRect(width / 2 - 80, height / 2 + 10, 160, 15, 0xBB000000, 1, 0xFFFFAA00);
            mc.fontRendererObj.drawString("OpenRouter", width / 2 - 30, height / 2 + 14, -1);
            
            mc.fontRendererObj.drawStringWithShadow("ESC: Cancel", width / 2 - 30, height / 2 + 30, 0xAAFFFFFF);
        }
        
        // Key Edit Overlay
        
        // Edit Text Overlay
        if (editingTextSetting != null) {
            String editMsg = "Editing " + editingTextSetting.getName() + ": " + editBuffer + "_";
            int mw = mc.fontRendererObj.getStringWidth(editMsg);
            RenderUtils.drawBorderedRect(width / 2 - (mw/2 + 10), height / 2 - 20, mw + 20, 30, 0xEE000000, 1, accent);
            mc.fontRendererObj.drawStringWithShadow(editMsg, width / 2 - mw/2, height / 2 - 10, -1);
            mc.fontRendererObj.drawStringWithShadow("Press ENTER to save, ESC to cancel", width / 2 - 80, height / 2 + 2, 0xAAFFFFFF);
        }
        
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
            File[] files = IEBClient.scriptManager.getScriptFiles();
            return 32 + (files == null ? 0 : files.length * 15);
        }
        int h = 0;
        for (Module m : IEBClient.moduleManager.getModulesByCategory(cat)) {
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

    private void drawAISidebar(int mouseX, int mouseY) {
        int x = width - sidebarWidth;
        RenderUtils.draw2DRect(x, 0, sidebarWidth, height, 0xEE111111);
        RenderUtils.draw2DRect(x, 0, 1, height, IEBClient.moduleManager.getModule(ClickGUIModule.class).accentColor.getColor());

        mc.fontRendererObj.drawStringWithShadow("IEB AI - Script Studio", x + 10, 10, -1);
        mc.fontRendererObj.drawStringWithShadow("§c[X]", width - 25, 10, -1);
        
        // Chats/History button
        RenderUtils.draw2DRect(x + 10, 25, 60, 14, 0x44FFFFFF);
        mc.fontRendererObj.drawStringWithShadow(showingChatHistory ? "§bBack" : "§6Chats", x + 15, 28, -1);

        if (showingChatHistory) {
            mc.fontRendererObj.drawStringWithShadow("§7Conversation History", x + 10, 45, -1);
            int hY = 60;
            // New Chat button
            RenderUtils.drawBorderedRect(x + 10, hY, sidebarWidth - 20, 14, 0xBB000000, 1, 0xFF55FF55);
            mc.fontRendererObj.drawString("+ START NEW CHAT", x + 30, hY + 3, 0xFF55FF55);
            hY += 20;

            for (Conversation conv : aiConversations) {
                int cColor = (conv == currentConversation) ? IEBClient.moduleManager.getModule(ClickGUIModule.class).accentColor.getColor() : -1;
                RenderUtils.draw2DRect(x + 10, hY, sidebarWidth - 20, 14, 0x33FFFFFF);
                mc.fontRendererObj.drawString(conv.title, x + 15, hY + 3, cColor);
                hY += 16;
                if (hY > height - 40) break;
            }
            return;
        }

        RenderUtils.drawBorderedRect(x + 75, 25, sidebarWidth - 85, 14, 0xBB000000, 1, modelDropdownOpen ? 0xFF55FFFF : -1);
        String displayModel = selectedModel.length() > 20 ? selectedModel.substring(0, 17) + "..." : selectedModel;
        mc.fontRendererObj.drawString(displayModel, x + 80, 28, 0xAAFFFFFF);

        int chatY = 45 - chatScroll;
        for (AIMessage msg : currentConversation.messages) {
            String prefix = msg.isUser ? "§bYou: §r" : "§dAI: §r";
            List<String> lines = mc.fontRendererObj.listFormattedStringToWidth(prefix + msg.text, sidebarWidth - 20);
            for (String line : lines) {
                if (chatY >= 40 && chatY < height - 50) {
                    mc.fontRendererObj.drawString(line, x + 10, chatY, -1);
                }
                chatY += 10;
            }
            if (!msg.isUser && msg.code != null) {
                if (chatY >= 40 && chatY < height - 50) {
                    RenderUtils.drawBorderedRect(x + 10, chatY, 110, 10, 0xBB000000, 1, 0xFF55FF55);
                    mc.fontRendererObj.drawString(msg.showingDetails ? "Hide Details" : "Show More Details", x + 15, chatY + 1, -1);
                }
                chatY += 14;
                if (msg.showingDetails) {
                    if (chatY >= 40 && chatY < height - 50) {
                        mc.fontRendererObj.drawString("Name: §a" + msg.name, x + 10, chatY, -1);
                    }
                    chatY += 10;
                    List<String> rawLines = mc.fontRendererObj.listFormattedStringToWidth("Raw AI: §7" + msg.raw, sidebarWidth - 30);
                    for (String rLine : rawLines) {
                        if (chatY >= 40 && chatY < height - 50) {
                            mc.fontRendererObj.drawString(rLine, x + 10, chatY, -1);
                        }
                        chatY += 10;
                    }
                }
            }
            chatY += 5;
        }

        // Draw an overlay over the header and input areas to ensure scroll doesn't bleed
        RenderUtils.draw2DRect(x, 0, sidebarWidth, 40, 0xFF111111);
        mc.fontRendererObj.drawString("IEB AI Script Studio", x + 10, 10, -1);
        mc.fontRendererObj.drawString("§c[X]§r", width - 20, 10, -1);
        RenderUtils.drawBorderedRect(x + 10, 25, sidebarWidth - 15, 12, 0xBB000000, 1, modelDropdownOpen ? 0xFF00AAFF : 0xFF555555);
        mc.fontRendererObj.drawString(displayModel, x + 15, 28, 0xAAFFFFFF);

        int inputY = height - 35;
        RenderUtils.draw2DRect(x, inputY - 5, sidebarWidth, 40, 0xFF111111);
        
        String iText = aiPrompt.isEmpty() ? (listeningAIInput ? "" : "Ask AI (e.g. Write a Fly hack)...") : aiPrompt + (listeningAIInput && (System.currentTimeMillis() / 500 % 2 == 0) ? "_" : "");
        List<String> promptLines = mc.fontRendererObj.listFormattedStringToWidth(iText, sidebarWidth - 20);
        
        int pBoxY = inputY;
        int pBoxHeight = Math.max(14, promptLines.size() * 10 + 4);
        RenderUtils.drawBorderedRect(x + 10, pBoxY, sidebarWidth - 20, pBoxHeight, 0xBB000000, 1, listeningAIInput ? 0xFF55FFFF : -1);
        
        for (int i = 0; i < promptLines.size(); i++) {
            mc.fontRendererObj.drawString(promptLines.get(i), x + 15, pBoxY + 3 + (i * 10), 0xAAFFFFFF);
        }

        if (modelDropdownOpen) {
            int dY = 39;
            List<String> models = dev.iebgames.iebclient.ai.AIConstants.ALL_MODELS;
            RenderUtils.draw2DRect(x + 10, dY, sidebarWidth - 20, 180, 0xEE000000);
            
            for (int i = 0; i < 15; i++) {
                int idx = i + modelScroll;
                if (idx >= models.size()) break;
                
                String mName = models.get(idx);
                boolean isOR = mName.startsWith("or:");
                String pureModel = isOR ? mName.substring(3) : mName.substring(5);
                
                int color = selectedModel.equals(pureModel) ? 0xFF55FFFF : -1;
                String label = (isOR ? "§e[OR] §r" : "§b[GR] §r") + pureModel;
                if (label.length() > 30) label = label.substring(0, 27) + "...";
                
                mc.fontRendererObj.drawString(label, x + 15, dY + 5 + (i * 10), color);
            }

            // API Key Edit button at bottom of dropdown context
            RenderUtils.drawBorderedRect(x + 10, dY + 160, sidebarWidth - 20, 15, 0xBB000000, 1, 0xFF00AAFF);
            mc.fontRendererObj.drawString("Manage API Keys", x + 45, dY + 164, -1);
        }
    }

    private void parseAIResponse(String content, String raw) {
        try {
            String showToUser = content;
            if (content.contains("showtouser:")) {
                showToUser = content.split("showtouser:")[1];
                if (showToUser.contains("code:")) showToUser = showToUser.split("code:")[0];
                if (showToUser.contains("name:")) showToUser = showToUser.split("name:")[0];
                showToUser = showToUser.trim();
            }

            String code = null;
            if (content.contains("code:")) {
                code = content.split("code:", 2)[1];
                if (code.contains("name:")) code = code.split("name:")[0];
                code = code.trim();
                code = code.replaceAll("```[a-zA-Z]*\\n?", "").replace("```", "").trim();
                if (code.equalsIgnoreCase("null") || code.isEmpty()) code = null;
            }

            String name = "generated_script";
            if (content.contains("name:")) {
                String potentialName = content.split("name:", 2)[1].trim().split("\\s")[0].trim().replaceAll("[^a-zA-Z0-9_]", "");
                if (!potentialName.isEmpty() && !potentialName.equalsIgnoreCase("null")) {
                    name = potentialName;
                }
            }

            if (code != null && !code.contains("function register")) {
                code = "function register(manager) {\n" + code + "\n}";
            }

            AIMessage msg = new AIMessage(showToUser, false);
            msg.code = code;
            msg.name = name;
            msg.raw = raw;
            currentConversation.messages.add(msg);
            saveAIHistory();

            if (code != null) {
                IEBClient.scriptManager.saveScript(name + ".js", code);
                IEBClient.addChatMessage("§a[AI] Script installed: §r" + name + ".js");
            }
        } catch (Exception e) {
            currentConversation.messages.add(new AIMessage("Parsing error: " + e.getMessage(), false));
        }
    }



    @Override
    public void handleMouseInput() throws IOException {
        NumberSetting scaleSetting = IEBClient.moduleManager.getModule(ClickGUIModule.class).scale;
        float scale = scaleSetting.getFloat();
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            int mouseX = Mouse.getEventX() * width / mc.displayWidth;
            int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
            mouseX /= scale;
            mouseY /= scale;

            if (aiSidebarOpen && mouseX >= width - sidebarWidth) {
                if (modelDropdownOpen) {
                    if (wheel > 0) modelScroll = Math.max(0, modelScroll - 1);
                    else modelScroll++;
                } else {
                    if (wheel > 0) chatScroll = Math.max(0, chatScroll - 20);
                    else chatScroll += 20;
                }
                return;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                double current = scaleSetting.getValue();
                if (wheel > 0) scaleSetting.setValue(Math.min(1.0, current + 0.05));
                else scaleSetting.setValue(Math.max(0.5, current - 0.05));
                return;
            }

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
            if (aiSidebarOpen && modelDropdownOpen) {
                int x = width - sidebarWidth;
                if (Mouse.getEventX() * width / mc.displayWidth >= x) {
                    if (wheel > 0) modelScroll = Math.max(0, modelScroll - 1);
                    else modelScroll = Math.min(dev.iebgames.iebclient.ai.AIConstants.ALL_MODELS.size() - 15, modelScroll + 1);
                }
            }
        }
        super.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        // 1. Sidebar Priority (Native Scale)
        if (aiSidebarOpen) {
            int x = width - sidebarWidth;
            // Larger X button hitbox
            if (mouseX >= width - 30 && mouseX <= width - 5 && mouseY >= 5 && mouseY <= 25) {
                aiSidebarOpen = false;
                modelDropdownOpen = false;
                showKeySelection = false;
                return;
            }
            if (mouseX >= x) {
                handleSidebarClick(mouseX, mouseY, mouseButton);
                return;
            }
        }

        // 2. Transformed Panel Coordinates
        float baseScale = IEBClient.moduleManager.getModule(ClickGUIModule.class).scale.getFloat();
        float finalScale = baseScale;
        if (aiSidebarOpen) {
            float sidebarRatio = (float)sidebarWidth / width;
            finalScale *= (1.0f - sidebarRatio);
        }
        
        int pMouseX = (int)(mouseX / finalScale);
        int pMouseY = (int)(mouseY / finalScale);

        if (pMouseX >= 20 && pMouseX <= 140 && pMouseY >= 2 && pMouseY <= 16) {
            listeningSearch = true;
            return;
        } else {
            listeningSearch = false;
        }

        // Reset Button click
        if (pMouseX >= 150 && pMouseX <= 210 && pMouseY >= 2 && pMouseY <= 16) {
            IEBClient.moduleManager.resetAll();
            panelX.clear();
            panelY.clear();
            return;
        }

        // Reload Scripts Button click
        if (pMouseX >= 220 && pMouseX <= 310 && pMouseY >= 2 && pMouseY <= 16) {
            IEBClient.scriptManager.loadScripts();
            return;
        }

        for (Category category : Category.values()) {
            int x = panelX.getOrDefault(category, 20 + category.ordinal() * 110);
            int y = panelY.getOrDefault(category, 20);
            
            // Drag detection
            if (pMouseX >= x && pMouseX <= x + 100 && pMouseY >= y && pMouseY <= y + 15) {
                draggingPanel = category;
                dragOffsetX = pMouseX - x;
                dragOffsetY = pMouseY - y;
                return;
            }

            int offset = scrollOffsets.getOrDefault(category, 0);
            int yStart = y + 17;
            int yPos = yStart - offset;
            
            if (category == Category.SCRIPTS) {
                // SCRIPT MAKER click
                if (pMouseX >= x && pMouseX <= x + 100 && pMouseY >= yPos && pMouseY <= yPos + 14 && pMouseY >= yStart && pMouseY < yStart + panelHeight) {
                    aiSidebarOpen = true;
                    return;
                }
                yPos += 16;
                // ADD SCRIPT click
                if (pMouseX >= x && pMouseX <= x + 100 && pMouseY >= yPos && pMouseY <= yPos + 14 && pMouseY >= yStart && pMouseY < yStart + panelHeight) {
                    addingScriptName = "";
                    return;
                }
                yPos += 16;
                
                File[] files = IEBClient.scriptManager.getScriptFiles();
                if (files != null) {
                    for (File f : files) {
                        if (pMouseX >= x && pMouseX <= x + 100 && pMouseY >= yPos && pMouseY <= yPos + 14 && pMouseY >= yStart && pMouseY < yStart + panelHeight) {
                            if (pMouseX >= x + 58 && pMouseX <= x + 70) {
                                mc.displayGuiScreen(new ScriptEditorScreen(f.getName(), this));
                                return;
                            } else if (pMouseX >= x + 72 && pMouseX <= x + 84) {
                                String dupName = f.getName().replace(".js", "") + "_copy.js";
                                IEBClient.scriptManager.duplicateScript(f.getName(), dupName);
                                return;
                            } else if (pMouseX >= x + 86 && pMouseX <= x + 98) {
                                IEBClient.scriptManager.deleteScript(f.getName());
                                return;
                            } else {
                                dev.iebgames.iebclient.script.ScriptModule m = IEBClient.scriptManager.getModuleForFile(f.getName());
                                if (m != null) m.toggle();
                                return;
                            }
                        }
                        yPos += 15;
                    }
                }
            } else {
                for (Module m : IEBClient.moduleManager.getModulesByCategory(category)) {
                    // ... existing module clicks ...
                    if (!searchQuery.isEmpty() && !m.getName().toLowerCase().contains(searchQuery.toLowerCase())) continue;

                    if (pMouseX >= x && pMouseX <= x + 100 && pMouseY >= yPos && pMouseY <= yPos + 14 && pMouseY >= yStart && pMouseY < yStart + panelHeight) {
                        if (m == bindingModule && pMouseX >= x + 70 && pMouseX <= x + 98) {
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
                            if (pMouseX >= x && pMouseX <= x + 100 && pMouseY >= yPos && pMouseY <= yPos + 12 && pMouseY >= yStart && pMouseY < yStart + panelHeight) {
                                if (mouseButton == 0) {
                                    if (s instanceof BooleanSetting) ((BooleanSetting) s).toggle();
                                    if (s instanceof ModeSetting) ((ModeSetting) s).next();
                                    if (s instanceof ColorSetting) expandedSettings.put(s, !expandedSettings.getOrDefault(s, false));
                                } else if (mouseButton == 2) {
                                    if (s instanceof NumberSetting) {
                                        editingSetting = (NumberSetting) s;
                                        editBuffer = s.getValue().toString();
                                        return;
                                    } else if (s instanceof TextSetting) {
                                        editingTextSetting = (TextSetting) s;
                                        editBuffer = (String) s.getValue();
                                        return;
                                    }
                                }
                                return;
                            }
                            yPos += 13;

                            if (s instanceof ColorSetting && expandedSettings.getOrDefault(s, false)) {
                                String[] comps = {"R", "G", "B", "A"};
                                for (int i = 0; i < 4; i++) {
                                    if (pMouseX >= x + 10 && pMouseX <= x + 90 && pMouseY >= yPos && pMouseY <= yPos + 11 && pMouseY >= yStart && pMouseY < yStart + panelHeight) {
                                        draggingSlider = s;
                                        draggingComponent = comps[i];
                                        return;
                                    }
                                    yPos += 11;
                                }
                                if (pMouseX >= x + 80 && pMouseX <= x + 88 && pMouseY >= yPos && pMouseY <= yPos + 10 && pMouseY >= yStart && pMouseY < yStart + panelHeight) {
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
        if (showKeySelection) {
            if (mouseX >= width / 2 - 80 && mouseX <= width / 2 + 80) {
                if (mouseY >= height / 2 - 10 && mouseY <= height / 2 + 5) {
                    editingKeyType = "Groq";
                    keyBuffer = dev.iebgames.iebclient.ai.AIManager.GROQ_KEY;
                    showKeySelection = false;
                    return;
                }
                if (mouseY >= height / 2 + 10 && mouseY <= height / 2 + 25) {
                    editingKeyType = "OpenRouter";
                    keyBuffer = dev.iebgames.iebclient.ai.AIManager.OPENROUTER_KEY;
                    showKeySelection = false;
                    return;
                }
            }
            return;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void handleSidebarClick(int mouseX, int mouseY, int mouseButton) {
        int x = width - sidebarWidth;
        int inputY = height - 35;
        
        // Chats button toggle
        if (mouseX >= x + 10 && mouseX <= x + 70 && mouseY >= 25 && mouseY <= 39) {
            showingChatHistory = !showingChatHistory;
            return;
        }

        if (showingChatHistory) {
            int hY = 60;
            // NEW CHAT click
            if (mouseX >= x + 10 && mouseX <= width - 10 && mouseY >= hY && mouseY <= hY + 14) {
                currentConversation = new Conversation();
                aiConversations.add(0, currentConversation);
                showingChatHistory = false;
                saveAIHistory();
                return;
            }
            hY += 20;

            for (Conversation conv : aiConversations) {
                if (mouseX >= x + 10 && mouseX <= width - 10 && mouseY >= hY && mouseY <= hY + 14) {
                    currentConversation = conv;
                    showingChatHistory = false;
                    return;
                }
                hY += 16;
                if (hY > height - 40) break;
            }
            return;
        }

        // Prompt box click
        String iText = aiPrompt.isEmpty() ? "Ask AI..." : aiPrompt;
        List<String> promptLines = mc.fontRendererObj.listFormattedStringToWidth(iText, sidebarWidth - 20);
        int pBoxHeight = Math.max(14, promptLines.size() * 10 + 4);
        
        if (mouseX >= x + 10 && mouseX <= width - 10 && mouseY >= inputY && mouseY <= inputY + pBoxHeight) {
            listeningAIInput = true;
            return;
        } else {
            listeningAIInput = false;
        }

        // Model dropdown click
        if (mouseX >= x + 75 && mouseX <= width - 10 && mouseY >= 25 && mouseY <= 39) {
            modelDropdownOpen = !modelDropdownOpen;
            return;
        }
        
        if (modelDropdownOpen) {
            int dY = 39;
            List<String> models = dev.iebgames.iebclient.ai.AIConstants.ALL_MODELS;
            for (int i = 0; i < 15; i++) {
                int idx = i + modelScroll;
                if (idx >= models.size()) break;
                if (mouseX >= x + 10 && mouseX <= width - 10 && mouseY >= dY + 5 + (i * 10) && mouseY <= dY + 15 + (i * 10)) {
                    String mName = models.get(idx);
                    if (mName.startsWith("groq:")) {
                        aiProvider = dev.iebgames.iebclient.ai.AIManager.Provider.GROQ;
                        selectedModel = mName.substring(5);
                    } else {
                        aiProvider = dev.iebgames.iebclient.ai.AIManager.Provider.OPENROUTER;
                        selectedModel = mName.substring(3);
                    }
                    modelDropdownOpen = false;
                    return;
                }
            }
            if (mouseX >= x + 10 && mouseX <= width - 10 && mouseY >= dY + 160 && mouseY <= dY + 175) {
                showKeySelection = true;
                modelDropdownOpen = false;
                return;
            }
            return;
        }
        
        // Show More Details click
        int chatY = 45 - chatScroll;
        for (AIMessage msg : currentConversation.messages) {
            String prefix = msg.isUser ? "§bYou: §r" : "§dAI: §r";
            List<String> lines = mc.fontRendererObj.listFormattedStringToWidth(prefix + msg.text, sidebarWidth - 20);
            chatY += lines.size() * 10;
            if (!msg.isUser && msg.code != null) {
                if (chatY >= 40 && chatY < height - 50) {
                    if (mouseX >= x + 10 && mouseX <= x + 120 && mouseY >= chatY && mouseY <= chatY + 12) {
                        msg.showingDetails = !msg.showingDetails;
                        return;
                    }
                }
                chatY += 14;
                if (msg.showingDetails) {
                    chatY += 10;
                    List<String> rawLines = mc.fontRendererObj.listFormattedStringToWidth("Raw AI: §7" + msg.raw, sidebarWidth - 30);
                    chatY += rawLines.size() * 10;
                }
            }
            chatY += 5;
        }
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
        if (showKeySelection && keyCode == Keyboard.KEY_ESCAPE) {
            showKeySelection = false;
            return;
        }
        if (editingKeyType != null) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                editingKeyType = null;
            } else if (keyCode == Keyboard.KEY_RETURN) {
                if (editingKeyType.equals("Groq")) dev.iebgames.iebclient.ai.AIManager.GROQ_KEY = keyBuffer;
                else dev.iebgames.iebclient.ai.AIManager.OPENROUTER_KEY = keyBuffer;
                dev.iebgames.iebclient.ai.AIKeyManager.save();
                editingKeyType = null;
            } else if (keyCode == Keyboard.KEY_BACK) {
                if (!keyBuffer.isEmpty()) keyBuffer = keyBuffer.substring(0, keyBuffer.length() - 1);
            } else if (typedChar != 0 && typedChar >= 32 && typedChar <= 126) {
                keyBuffer += typedChar;
            }
            return;
        }

        if (listeningAIInput) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                listeningAIInput = false;
            } else if (keyCode == Keyboard.KEY_RETURN) {
                if (!aiPrompt.isEmpty()) {
                    String prompt = aiPrompt;
                    currentConversation.messages.add(new AIMessage(prompt, true));
                    aiPrompt = "";
                    
                    // Auto naming if first message
                    if (currentConversation.messages.size() == 1) {
                        aiManager.getChatSummary(prompt, aiProvider, selectedModel, summary -> currentConversation.title = summary);
                    }

                    java.util.List<dev.iebgames.iebclient.ai.AIManager.ChatMessage> history = new java.util.ArrayList<>();
                    for (AIMessage m : currentConversation.messages) {
                        history.add(new dev.iebgames.iebclient.ai.AIManager.ChatMessage(m.role, m.text));
                    }

                    aiManager.generateScript(history, aiProvider, selectedModel, mc.getSession().getUsername(), new dev.iebgames.iebclient.ai.AIManager.AIResponseCallback() {
                        @Override
                        public void onSuccess(String content, String raw) {
                            parseAIResponse(content, raw);
                        }
                        @Override
                        public void onFailure(String error) {
                            currentConversation.messages.add(new AIMessage("§cError: " + error, false));
                        }
                    });
                }
            } else if (keyCode == Keyboard.KEY_BACK) {
                if (!aiPrompt.isEmpty()) aiPrompt = aiPrompt.substring(0, aiPrompt.length() - 1);
            } else if (typedChar != 0 && typedChar >= 32) {
                aiPrompt += typedChar;
            }
            return;
        }

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

        if (editingTextSetting != null) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                editingTextSetting = null;
            } else if (keyCode == Keyboard.KEY_RETURN) {
                editingTextSetting.setValue(editBuffer);
                editingTextSetting = null;
            } else if (keyCode == Keyboard.KEY_BACK) {
                if (!editBuffer.isEmpty()) editBuffer = editBuffer.substring(0, editBuffer.length() - 1);
            } else if (typedChar != 0 && typedChar >= 32 && typedChar <= 126) {
                editBuffer += typedChar;
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
                if (!addingScriptName.isEmpty()) IEBClient.scriptManager.createScript(addingScriptName);
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
                IEBClient.scriptManager.saveScript(editingScriptName, content);
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
