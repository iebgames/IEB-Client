package dev.iebgames.iebclient.script;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.Category;
import net.minecraft.client.Minecraft;

import javax.script.*;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class ScriptManager {

    private final List<ScriptModule> scriptModules = new ArrayList<>();
    private final Map<String, String> lastErrors = new HashMap<>();
    private final Map<String, ScriptModule> fileToModule = new HashMap<>();
    private File scriptsFolder;
    private String currentLoadingFile;

    private File getScriptsFolder() {
        if (scriptsFolder == null) {
            scriptsFolder = new File(Minecraft.getMinecraft().mcDataDir, "ieb" + File.separator + "scripts");
            if (!scriptsFolder.exists()) scriptsFolder.mkdirs();
        }
        return scriptsFolder;
    }

    public void loadScripts() {
        for (ScriptModule m : new ArrayList<>(scriptModules)) {
            if (m.isEnabled()) m.setEnabled(false);
            IEBClient.moduleManager.getModules().remove(m);
        }
        scriptModules.clear();
        fileToModule.clear();
        lastErrors.clear();

        File[] files = getScriptsFolder().listFiles((dir, name) -> name.endsWith(".js"));
        if (files == null) return;

        int loaded = 0;
        for (File file : files) {
            currentLoadingFile = file.getName();
            try {
                ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
                if (engine == null) {
                    lastErrors.put(file.getName(), "Nashorn engine unavailable (requires Java 8)");
                    continue;
                }

                Bindings bindings = engine.createBindings();
                bindings.put("mc", Minecraft.getMinecraft());
                bindings.put("API", new ScriptAPI());
                engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

                try (FileReader reader = new FileReader(file)) {
                    engine.eval(reader);
                }

                Invocable inv = (Invocable) engine;
                Object result = inv.invokeFunction("register", this);
                if (result != null) {
                    // register() returned directly — ignore
                }
                lastErrors.remove(file.getName());
                loaded++;
            } catch (Exception e) {
                String error = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
                lastErrors.put(file.getName(), error);
                IEBClient.addChatMessage("§cScript error [" + file.getName() + "]: " + error);
                e.printStackTrace();
            }
        }
        currentLoadingFile = null;
        if (loaded > 0) {
            IEBClient.addChatMessage("§aLoaded §r" + loaded + "§a script(s).");
        }
    }

    public void registerModule(String name, String category, String description, int key, Object callback) {
        Category cat = Category.SCRIPTS;
        if (category != null && !category.isEmpty()) {
            try {
                cat = Category.valueOf(category.toUpperCase());
            } catch (Exception ignored) {
                cat = Category.SCRIPTS;
            }
        }

        String fileName = currentLoadingFile != null ? currentLoadingFile : (name + ".js");
        ScriptModule m = new ScriptModule(name, description, cat, key, callback, fileName);
        scriptModules.add(m);
        fileToModule.put(fileName, m);
        IEBClient.moduleManager.getModules().add(m);
    }

    public List<ScriptModule> getScriptModules() {
        return scriptModules;
    }

    public ScriptModule getModuleForFile(String fileName) {
        return fileToModule.get(fileName);
    }

    public File[] getScriptFiles() {
        File[] files = getScriptsFolder().listFiles((dir, name) -> name.endsWith(".js"));
        return files != null ? files : new File[0];
    }

    public void createScript(String name) {
        if (!name.endsWith(".js")) name += ".js";
        File file = new File(getScriptsFolder(), name);
        if (file.exists()) {
            IEBClient.addChatMessage("§cScript already exists: " + name);
            return;
        }

        try {
            String baseName = name.replace(".js", "");
            List<String> lines = Arrays.asList(
                "function register(manager) {",
                "    manager.registerModule(\"" + baseName + "\", \"SCRIPTS\", \"Custom scripted module\", 0, {",
                "        onEnable: function() {",
                "            API.chat(\"§a" + baseName + " enabled!\");",
                "        },",
                "        onDisable: function() {",
                "            API.chat(\"§c" + baseName + " disabled!\");",
                "        },",
                "        onUpdate: function() {",
                "            if (!API.isInGame()) return;",
                "            // Write your logic here",
                "            // Example: mc.thePlayer.capabilities.isFlying = true;",
                "        }",
                "    });",
                "}"
            );
            Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
            loadScripts();
            IEBClient.addChatMessage("§aCreated script: §r" + name);
        } catch (Exception e) {
            IEBClient.addChatMessage("§cFailed to create script: " + e.getMessage());
        }
    }

    public void duplicateScript(String sourceName, String newName) {
        if (!newName.endsWith(".js")) newName += ".js";
        File target = new File(getScriptsFolder(), newName);
        if (target.exists()) {
            IEBClient.addChatMessage("§cScript already exists: " + newName);
            return;
        }
        try {
            String content = getScriptContent(sourceName);
            Files.write(target.toPath(), content.getBytes(StandardCharsets.UTF_8));
            loadScripts();
            IEBClient.addChatMessage("§aDuplicated to: §r" + newName);
        } catch (Exception e) {
            IEBClient.addChatMessage("§cDuplicate failed: " + e.getMessage());
        }
    }

    public void deleteScript(String name) {
        File file = new File(getScriptsFolder(), name);
        if (file.exists() && file.delete()) {
            loadScripts();
            IEBClient.addChatMessage("§aDeleted script: §r" + name);
        } else {
            IEBClient.addChatMessage("§cFailed to delete script: " + name);
        }
    }

    public String getScriptContent(String name) {
        File file = new File(getScriptsFolder(), name);
        try {
            return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "// Error reading file: " + e.getMessage() + "\nfunction register(manager) {\n}\n";
        }
    }

    public void saveScript(String name, String content) {
        File file = new File(getScriptsFolder(), name);
        try {
            Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
            loadScripts();
            IEBClient.addChatMessage("§aSaved & reloaded: §r" + name);
        } catch (Exception e) {
            IEBClient.addChatMessage("§cFailed to save script: " + e.getMessage());
        }
    }

    public String getLastError(String name) {
        return lastErrors.get(name);
    }

    public boolean hasError(String name) {
        return lastErrors.containsKey(name);
    }

    public File getScriptsDirectory() {
        return getScriptsFolder();
    }
}
