package dev.iebgames.iebclient.ai;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class AIKeyManager {

    private static final Gson GSON = new Gson();

    private AIKeyManager() {}

    private static File getKeyFile() {
        return new File(Minecraft.getMinecraft().mcDataDir, "ieb/keys.json");
    }

    public static void load() {
        try {
            File file = getKeyFile();
            if (!file.exists()) return;
            String json = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            JsonObject obj = GSON.fromJson(json, JsonObject.class);
            if (obj.has("groq")) AIManager.GROQ_KEY = obj.get("groq").getAsString();
            if (obj.has("openrouter")) AIManager.OPENROUTER_KEY = obj.get("openrouter").getAsString();
        } catch (Exception ignored) {}
    }

    public static void save() {
        try {
            File file = getKeyFile();
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            JsonObject obj = new JsonObject();
            obj.addProperty("groq", AIManager.GROQ_KEY != null ? AIManager.GROQ_KEY : "");
            obj.addProperty("openrouter", AIManager.OPENROUTER_KEY != null ? AIManager.OPENROUTER_KEY : "");
            Files.write(file.toPath(), GSON.toJson(obj).getBytes(StandardCharsets.UTF_8));
        } catch (Exception ignored) {}
    }
}
