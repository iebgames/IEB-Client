package dev.iebgames.iebclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.*;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {

    private final File configFile;
    private final Gson gson;

    public ConfigManager() {
        File dir = new File(Minecraft.getMinecraft().mcDataDir, "ieb-client");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        configFile = new File(dir, "config.json");
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void save() {
        try {
            JsonObject json = new JsonObject();
            for (Module m : IEBClient.INSTANCE.moduleManager.getModules()) {
                JsonObject moduleJson = new JsonObject();
                moduleJson.addProperty("enabled", m.isEnabled());
                moduleJson.addProperty("keybind", m.getKeybind());

                JsonObject settingsJson = new JsonObject();
                for (Setting<?> s : m.getSettings()) {
                    if (s instanceof BooleanSetting) {
                        settingsJson.addProperty(s.getName(), (Boolean) s.getValue());
                    } else if (s instanceof NumberSetting) {
                        settingsJson.addProperty(s.getName(), ((NumberSetting) s).getValue());
                    } else if (s instanceof ModeSetting) {
                        settingsJson.addProperty(s.getName(), (String) s.getValue());
                    } else if (s instanceof ColorSetting) {
                        ColorSetting cs = (ColorSetting) s;
                        settingsJson.addProperty(s.getName() + "_R", cs.getRed());
                        settingsJson.addProperty(s.getName() + "_G", cs.getGreen());
                        settingsJson.addProperty(s.getName() + "_B", cs.getBlue());
                        settingsJson.addProperty(s.getName() + "_A", cs.getAlpha());
                        settingsJson.addProperty(s.getName() + "_Chroma", cs.isChroma());
                    }
                }
                moduleJson.add("settings", settingsJson);
                json.add(m.getName(), moduleJson);
            }

            FileWriter writer = new FileWriter(configFile);
            gson.toJson(json, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        if (!configFile.exists()) return;
        try {
            FileReader reader = new FileReader(configFile);
            JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
            reader.close();

            for (Module m : IEBClient.INSTANCE.moduleManager.getModules()) {
                if (json.has(m.getName())) {
                    JsonObject moduleJson = json.getAsJsonObject(m.getName());
                    if (moduleJson.has("enabled")) m.setEnabled(moduleJson.get("enabled").getAsBoolean());
                    if (moduleJson.has("keybind")) m.setKeybind(moduleJson.get("keybind").getAsInt());

                    if (moduleJson.has("settings")) {
                        JsonObject settingsJson = moduleJson.getAsJsonObject("settings");
                        for (Setting<?> s : m.getSettings()) {
                            if (s instanceof BooleanSetting && settingsJson.has(s.getName())) {
                                ((BooleanSetting) s).setValue(settingsJson.get(s.getName()).getAsBoolean());
                            } else if (s instanceof NumberSetting && settingsJson.has(s.getName())) {
                                ((NumberSetting) s).setValue(settingsJson.get(s.getName()).getAsDouble());
                            } else if (s instanceof ModeSetting && settingsJson.has(s.getName())) {
                                ((ModeSetting) s).setValue(settingsJson.get(s.getName()).getAsString());
                            } else if (s instanceof ColorSetting) {
                                ColorSetting cs = (ColorSetting) s;
                                if (settingsJson.has(s.getName() + "_R")) cs.setRed(settingsJson.get(s.getName() + "_R").getAsInt());
                                if (settingsJson.has(s.getName() + "_G")) cs.setGreen(settingsJson.get(s.getName() + "_G").getAsInt());
                                if (settingsJson.has(s.getName() + "_B")) cs.setBlue(settingsJson.get(s.getName() + "_B").getAsInt());
                                if (settingsJson.has(s.getName() + "_A")) cs.setAlpha(settingsJson.get(s.getName() + "_A").getAsInt());
                                if (settingsJson.has(s.getName() + "_Chroma")) cs.setChroma(settingsJson.get(s.getName() + "_Chroma").getAsBoolean());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
