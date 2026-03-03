package dev.iebgames.iebclient;

import dev.iebgames.iebclient.event.EventBus;
import dev.iebgames.iebclient.gui.ClickGUI;
import dev.iebgames.iebclient.module.ModuleManager;
import dev.iebgames.iebclient.util.Logger;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = IEBClient.MODID, name = IEBClient.NAME, version = IEBClient.VERSION)
public class IEBClient {

    public static final String MODID   = "iebclient";
    public static final String NAME    = "IEB Client";
    public static final String VERSION = "1.0";

    @Mod.Instance
    public static IEBClient INSTANCE;

    // Core singletons
    public static EventBus      eventBus;
    public static ModuleManager moduleManager;
    public static ClickGUI      clickGUI;

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
        INSTANCE = this; // Set instance here
        init(); // Call the new init method
    }

    public void init() {
        moduleManager.init();
        eventBus.register(this);
        System.out.println("IEB Client initialized successfully!");
    }

    @EventHook
    public void onKey(EventKey e) {
        if (e.getKey() == Keyboard.KEY_TAB) {
            Minecraft.getMinecraft().displayGuiScreen(new ClickGUI());
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
