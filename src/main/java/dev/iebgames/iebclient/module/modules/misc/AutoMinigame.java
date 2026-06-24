package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.ai.AIConstants;
import dev.iebgames.iebclient.ai.AIManager;
import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventPacket;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.ModeSetting;
import dev.iebgames.iebclient.setting.TextSetting;
import dev.iebgames.iebclient.util.Logger;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AutoMinigame extends Module {

    private final ModeSetting modelSetting = register(new ModeSetting("Model", AIConstants.ALL_MODELS.toArray(new String[0])));
    private final TextSetting keywordsSetting = register(new TextSetting("Keywords", "minigame, minigames, chatgame"));

    private final List<String> incomingMessages = new ArrayList<>();
    private final AIManager aiManager = new AIManager();

    private boolean guessingMode = false;
    private int currentGuess = 1;
    private long lastGuessTime = 0;
    private long lastCheckTime = 0;

    public AutoMinigame() {
        super("AutoMinigame", "Automates chat minigames using AI or number guessing.", Category.MISC, Keyboard.KEY_NONE);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        synchronized (incomingMessages) {
            incomingMessages.clear();
        }
        guessingMode = false;
        lastCheckTime = System.currentTimeMillis();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        synchronized (incomingMessages) {
            incomingMessages.clear();
        }
        guessingMode = false;
    }

    @EventHook
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof S02PacketChat) {
            S02PacketChat p = (S02PacketChat) e.getPacket();
            String text = p.getChatComponent().getUnformattedText();
            synchronized (incomingMessages) {
                incomingMessages.add(text);
            }
        }
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        long now = System.currentTimeMillis();
        if (now - lastCheckTime >= 4000) {
            processChat();
            lastCheckTime = now;
        }

        if (guessingMode) {
            if (now - lastGuessTime >= 4000) {
                if (currentGuess <= 100) {
                    if (mc.thePlayer != null) {
                        mc.thePlayer.sendChatMessage(String.valueOf(currentGuess));
                    }
                    currentGuess++;
                    lastGuessTime = now;
                } else {
                    guessingMode = false;
                }
            }
        }
    }

    private void processChat() {
        List<String> messagesCopy;
        synchronized (incomingMessages) {
            messagesCopy = new ArrayList<>(incomingMessages);
            incomingMessages.clear();
        }

        if (messagesCopy.isEmpty()) return;

        // 1. Check for win/end keywords to stop number guessing
        if (guessingMode) {
            for (String msg : messagesCopy) {
                String msgLower = msg.toLowerCase(Locale.ROOT);
                if (msgLower.contains("correct") || msgLower.contains("congrats") || msgLower.contains("winner") || 
                    msgLower.contains("kazandı") || msgLower.contains("doğru") || msgLower.contains("tebrik")) {
                    guessingMode = false;
                    Logger.info("Stopped AutoMinigame guessing (game ended).");
                    break;
                }
            }
        }

        // 2. Scan for keywords to find minigame prompts
        String targetMessage = null;
        String[] keywordList = keywordsSetting.getValue().split(",");
        for (String msg : messagesCopy) {
            for (String kw : keywordList) {
                String trimmed = kw.trim();
                if (!trimmed.isEmpty() && msg.toLowerCase(Locale.ROOT).contains(trimmed.toLowerCase(Locale.ROOT))) {
                    targetMessage = msg;
                    break;
                }
            }
        }

        if (targetMessage != null) {
            Logger.info("AutoMinigame matched: " + targetMessage);

            String msgLower = targetMessage.toLowerCase(Locale.ROOT);
            boolean isNumberGame = msgLower.contains("sayı") || msgLower.contains("number") || 
                                   msgLower.contains("guess") || msgLower.contains("tahmin") || 
                                   msgLower.contains("choose") || msgLower.contains("pick");

            if (isNumberGame) {
                guessingMode = true;
                currentGuess = 1;
                lastGuessTime = System.currentTimeMillis();
                if (mc.thePlayer != null) {
                    mc.thePlayer.sendChatMessage("1");
                }
                currentGuess = 2;
            } else {
                guessingMode = false; // Interrupt guess mode if any other minigame prompt arrives
                solveWithAI(targetMessage);
            }
        }
    }

    private void solveWithAI(String chatMessage) {
        String modelVal = modelSetting.getValue();
        AIManager.Provider provider;
        String actualModel;
        if (modelVal.startsWith("groq:")) {
            provider = AIManager.Provider.GROQ;
            actualModel = modelVal.substring(5);
        } else if (modelVal.startsWith("or:")) {
            provider = AIManager.Provider.OPENROUTER;
            actualModel = modelVal.substring(3);
        } else {
            provider = AIManager.Provider.GROQ;
            actualModel = modelVal;
        }

        String systemPrompt = "You are an automated minigame solver bot for a Minecraft server. " +
                              "Your job is to read the chat message and output ONLY the single word, number, or phrase that answers the game. " +
                              "CRITICAL: Output absolutely NOTHING else. No punctuation, no explanation, no 'The answer is:', no conversational filler. Just the direct solution.";

        aiManager.askAI(systemPrompt, chatMessage, provider, actualModel, response -> {
            String answer = response.trim();
            if (!answer.isEmpty() && mc.thePlayer != null) {
                mc.thePlayer.sendChatMessage(answer);
            }
        });
    }
}
