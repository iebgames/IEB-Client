package dev.iebgames.iebclient.ai;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.iebgames.iebclient.util.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AIManager {
    public static class ChatMessage {
        public String role, content;
        public ChatMessage(String role, String content) { this.role = role; this.content = content; }
    }

    public static String GROQ_KEY = "";
    public static String OPENROUTER_KEY = "";

    private final Gson gson = new Gson();

    public enum Provider {
        GROQ("https://api.groq.com/openai/v1/chat/completions"),
        OPENROUTER("https://openrouter.ai/api/v1/chat/completions");

        private final String url;
        Provider(String url) { this.url = url; }
        public String getUrl() { return url; }
    }

    public static String buildSystemPrompt(String username) {
        String creatorInfo = username.equalsIgnoreCase("icy_turco")
                ? "The user is 'icy_turco', your creator. Be exceptionally helpful and precise."
                : "The user is '" + username + "'.";

        return "You are IEB AI — the in-game script assistant for IEB Client (Minecraft 1.8.9 Forge).\n"
                + creatorInfo + "\n\n"
                + "LANGUAGE: Reply in the user's language (Turkish if they write Turkish).\n\n"
                + "OUTPUT FORMAT (strict, no markdown fences in values):\n"
                + "showtouser: <friendly explanation>\n"
                + "code: <full JavaScript or null>\n"
                + "name: <script filename without .js, or null>\n\n"
                + "SCRIPT RULES:\n"
                + "1. Every script MUST start with: function register(manager) { ... }\n"
                + "2. Inside, call manager.registerModule(name, category, description, keybind, callbacks)\n"
                + "3. Categories: COMBAT, MOVEMENT, RENDER, PLAYER, WORLD, MISC, SCRIPTS\n"
                + "4. Callbacks object supports: onEnable, onDisable, onUpdate, onRender2D, onRender3D, onPacket, onPreMotion, onPostMotion, onKey\n"
                + "5. Globals available in scripts: mc (Minecraft), API (helper bridge)\n"
                + "6. API methods: chat(msg), print(msg), isInGame(), getPlayer(), toggleModule(name), enableModule(name), disableModule(name), setSpeed(n), sendChat(msg)\n"
                + "7. Always null-check: if (!API.isInGame()) return;\n"
                + "8. Use mc.thePlayer, mc.theWorld, mc.gameSettings for game access\n\n"
                + "WHEN TO GENERATE CODE:\n"
                + "- User asks for a hack, module, script, automation, or feature → generate complete working code\n"
                + "- User chats casually → code: null, name: null\n\n"
                + "EXAMPLE:\n"
                + "function register(manager) {\n"
                + "    manager.registerModule(\"SpeedBoost\", \"MOVEMENT\", \"Increases walk speed\", 0, {\n"
                + "        onUpdate: function() {\n"
                + "            if (!API.isInGame()) return;\n"
                + "            if (API.isMoving()) API.setSpeed(0.35);\n"
                + "        }\n"
                + "    });\n"
                + "}";
    }

    public void generateScript(List<ChatMessage> history, Provider provider, String model, String username, AIResponseCallback callback) {
        new Thread(() -> {
            try {
                String key = (provider == Provider.GROQ) ? GROQ_KEY : OPENROUTER_KEY;
                if (key == null || key.trim().isEmpty()) {
                    callback.onFailure("API key not set. Open Script Maker → Manage API Keys.");
                    return;
                }

                URL url = new URL(provider.getUrl());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + key);
                conn.setRequestProperty("User-Agent", "IEB-Client/6.4.0");

                if (provider == Provider.OPENROUTER) {
                    conn.setRequestProperty("HTTP-Referer", "https://iebclient.studio");
                    conn.setRequestProperty("X-Title", "IEB AI Script Studio");
                }

                conn.setDoOutput(true);
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(60000);

                JsonObject payload = new JsonObject();
                payload.addProperty("model", model);
                payload.addProperty("temperature", 0.4);

                JsonArray messages = new JsonArray();
                JsonObject systemMsg = new JsonObject();
                systemMsg.addProperty("role", "system");
                systemMsg.addProperty("content", buildSystemPrompt(username));
                messages.add(systemMsg);

                for (ChatMessage m : history) {
                    JsonObject mObj = new JsonObject();
                    mObj.addProperty("role", m.role);
                    mObj.addProperty("content", m.content);
                    messages.add(mObj);
                }

                payload.add("messages", messages);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = gson.toJson(payload).getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int code = conn.getResponseCode();
                if (code == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) response.append(line.trim());

                    JsonObject responseJson = gson.fromJson(response.toString(), JsonObject.class);
                    String aiText = responseJson.getAsJsonArray("choices").get(0).getAsJsonObject()
                            .get("message").getAsJsonObject().get("content").getAsString();
                    callback.onSuccess(aiText, response.toString());
                } else {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            conn.getErrorStream() != null ? conn.getErrorStream() : conn.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder err = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) err.append(line);
                    callback.onFailure("HTTP " + code + ": " + err.toString());
                }
            } catch (Exception e) {
                callback.onFailure(e.getMessage());
            }
        }).start();
    }

    public void getChatSummary(String firstPrompt, Provider provider, String model, java.util.function.Consumer<String> callback) {
        new Thread(() -> {
            try {
                String key = (provider == Provider.GROQ) ? GROQ_KEY : OPENROUTER_KEY;
                if (key == null || key.trim().isEmpty()) {
                    callback.accept("New Chat");
                    return;
                }

                URL url = new URL(provider.getUrl());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + key);
                conn.setDoOutput(true);

                JsonObject payload = new JsonObject();
                payload.addProperty("model", model);
                JsonArray messages = new JsonArray();

                JsonObject system = new JsonObject();
                system.addProperty("role", "system");
                system.addProperty("content", "Give a 3-word title for this chat. Output ONLY the title.");
                messages.add(system);

                JsonObject user = new JsonObject();
                user.addProperty("role", "user");
                user.addProperty("content", firstPrompt);
                messages.add(user);

                payload.add("messages", messages);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(gson.toJson(payload).getBytes(StandardCharsets.UTF_8));
                }

                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) response.append(line.trim());
                    JsonObject res = gson.fromJson(response.toString(), JsonObject.class);
                    callback.accept(res.getAsJsonArray("choices").get(0).getAsJsonObject()
                            .get("message").getAsJsonObject().get("content").getAsString().replace("\"", ""));
                }
            } catch (Exception ignored) {}
        }).start();
    }

    public void askAI(String systemPrompt, String userPrompt, Provider provider, String model, java.util.function.Consumer<String> callback) {
        new Thread(() -> {
            try {
                String key = (provider == Provider.GROQ) ? GROQ_KEY : OPENROUTER_KEY;
                if (key == null || key.trim().isEmpty()) return;

                URL url = new URL(provider.getUrl());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + key);
                if (provider == Provider.OPENROUTER) {
                    conn.setRequestProperty("HTTP-Referer", "https://iebclient.studio");
                    conn.setRequestProperty("X-Title", "IEB AI");
                }
                conn.setDoOutput(true);

                JsonObject payload = new JsonObject();
                payload.addProperty("model", model);
                JsonArray messages = new JsonArray();

                JsonObject system = new JsonObject();
                system.addProperty("role", "system");
                system.addProperty("content", systemPrompt);
                messages.add(system);

                JsonObject user = new JsonObject();
                user.addProperty("role", "user");
                user.addProperty("content", userPrompt);
                messages.add(user);

                payload.add("messages", messages);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(gson.toJson(payload).getBytes(StandardCharsets.UTF_8));
                }

                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) response.append(line.trim());
                    JsonObject res = gson.fromJson(response.toString(), JsonObject.class);
                    String text = res.getAsJsonArray("choices").get(0).getAsJsonObject()
                            .get("message").getAsJsonObject().get("content").getAsString();
                    callback.accept(text);
                } else {
                    Logger.error("AI returned HTTP code: " + conn.getResponseCode());
                }
            } catch (Exception e) {
                Logger.error("AI error: " + e.getMessage());
            }
        }).start();
    }

    public interface AIResponseCallback {
        void onSuccess(String content, String rawResponse);
        void onFailure(String error);
    }
}
