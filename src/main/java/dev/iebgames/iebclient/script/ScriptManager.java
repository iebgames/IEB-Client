package dev.iebgames.iebclient.script;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.client.Minecraft;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ScriptManager {

    private final List<ScriptModule> scriptModules = new ArrayList<>();
    private final ScriptEngine engine;
    private final File scriptsFolder;

    public ScriptManager() {
        ScriptEngineManager factory = new ScriptEngineManager();
        this.engine = factory.getEngineByName("nashorn");
        this.scriptsFolder = new File(Minecraft.getMinecraft().mcDataDir, "ieb" + File.separator + "scripts");
        
        if (!scriptsFolder.exists()) scriptsFolder.mkdirs();
    }

    public void loadScripts() {
        // Clear existing script modules
        for (ScriptModule m : scriptModules) {
            IEBClient.INSTANCE.moduleManager.getModules().remove(m);
        }
        scriptModules.clear();

        File[] files = scriptsFolder.listFiles((dir, name) -> name.endsWith(".js"));
        if (files == null) return;

        for (File file : files) {
            try {
                engine.eval(new FileReader(file));
                Invocable inv = (Invocable) engine;
                // We expect a 'register()' function in the JS
                inv.invokeFunction("register", this);
            } catch (Exception e) {
                IEBClient.addChatMessage("§cError loading script " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    public void registerModule(String name, String category, String description, int key, Object callback) {
        Category cat;
        try {
            cat = Category.valueOf(category.toUpperCase());
        } catch (Exception e) {
            cat = Category.MISC;
        }
        
        ScriptModule m = new ScriptModule(name, description, cat, key, callback);
        scriptModules.add(m);
        IEBClient.INSTANCE.moduleManager.getModules().add(m);
        IEBClient.addChatMessage("§aLoaded script module: §r" + name);
    }

    public List<ScriptModule> getScriptModules() {
        return scriptModules;
    }

    public File[] getScriptFiles() {
        return scriptsFolder.listFiles((dir, name) -> name.endsWith(".js"));
    }

    public void createScript(String name) {
        if (!name.endsWith(".js")) name += ".js";
        File file = new File(scriptsFolder, name);
        if (file.exists()) return;
        
        try {
            List<String> lines = new ArrayList<>();
            lines.add("function register(manager) {");
            lines.add("    // " + name + " - Template");
            lines.add("    manager.registerModule(\"" + name.replace(".js", "") + "\", \"MISC\", \"Scripted module\", 0, function(event) {");
            lines.add("        // Logic here");
            lines.add("    });");
            lines.add("}");
            Files.write(file.toPath(), lines);
            loadScripts();
        } catch (Exception e) {
            IEBClient.addChatMessage("§cFailed to create script: " + e.getMessage());
        }
    }

    public void deleteScript(String name) {
        File file = new File(scriptsFolder, name);
        if (file.exists() && file.delete()) {
            loadScripts();
        } else {
            IEBClient.addChatMessage("§cFailed to delete script: " + name);
        }
    }

    public String getScriptContent(String name) {
        File file = new File(scriptsFolder, name);
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            return "// Error reading file";
        }
    }

    public void saveScript(String name, String content) {
        File file = new File(scriptsFolder, name);
        try {
            Files.write(file.toPath(), content.getBytes());
            loadScripts();
        } catch (Exception e) {
            IEBClient.addChatMessage("§cFailed to save script: " + e.getMessage());
        }
    }
}
