package dev.iebgames.iebclient;

import dev.iebgames.iebclient.event.EventBus;
import dev.iebgames.iebclient.gui.ClickGUI;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.module.ModuleManager;
import dev.iebgames.iebclient.util.Logger;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventKey;
import dev.iebgames.iebclient.event.events.EventRender2D;
import dev.iebgames.iebclient.ui.HUD;
import org.lwjgl.input.Keyboard;
import dev.iebgames.iebclient.config.ConfigManager;
import dev.iebgames.iebclient.script.ScriptManager;

@Mod(modid = IEBClient.MODID, name = IEBClient.NAME, version = IEBClient.VERSION)
public class IEBClient {

    public static final String MODID   = "iebclient";
    public static final String NAME    = "IEB Client";
    public static final String VERSION = "v6.2.30-FIXED.94.1.1";

    @Mod.Instance
    public static IEBClient INSTANCE;

    // Core singletons
    public static EventBus      eventBus;
    public static ModuleManager moduleManager;
    public static ScriptManager scriptManager;
    public static ClickGUI      clickGUI;
    public static HUD           hud;
    public static ConfigManager configManager;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Logger.info("IEB Client v" + VERSION + " by IEB Games - PreInit");
        eventBus = new EventBus();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Logger.info("IEB Client - Init: loading modules...");
        moduleManager = new ModuleManager();
        clickGUI      = new ClickGUI();
        hud           = new HUD();
        INSTANCE = this; // Set instance here
        onClientInit(); // Renamed to avoid recursion
    }

    public void onClientInit() {
        scriptManager = new ScriptManager();
        moduleManager.init();
        scriptManager.loadScripts();
        eventBus.register(this);
        
        configManager = new ConfigManager();
        configManager.load();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            configManager.save();
        }));
        
        System.out.println("IEB Client initialized successfully!");
    }

    public static void addChatMessage(String message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText("§8[§bIEB§8] §f" + message));
        }
    }

    @EventHook
    public void onKey(EventKey e) {
        for (Module m : moduleManager.getModules()) {
            if (m.getKeybind() == e.getKey()) {
                m.toggle();
            }
        }
    }

    @EventHook
    public void onRender2D(EventRender2D e) {
        hud.draw();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Logger.info("IEB Client - PostInit: ready.");
    }
}
