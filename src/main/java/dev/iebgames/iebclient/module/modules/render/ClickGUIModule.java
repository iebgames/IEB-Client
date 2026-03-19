package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.ColorSetting;
import dev.iebgames.iebclient.setting.NumberSetting;
import org.lwjgl.input.Keyboard;

public class ClickGUIModule extends Module {

    public final ColorSetting bgColor = register(new ColorSetting("Background", 0x88000000));
    public final ColorSetting textColor = register(new ColorSetting("Text", -1));
    public final ColorSetting accentColor = register(new ColorSetting("Accent", 0xFFFF0000));
    public final NumberSetting scale = register(new NumberSetting("Scale", 0.6, 0.5, 1.0, 0.05));
    
    public final ColorSetting crosshairColor = register(new ColorSetting("Crosshair Color", 0xFF00FF00));
    public final ColorSetting blockOverlayColor = register(new ColorSetting("Block Overlay Color", 0x88FF0000));

    public ClickGUIModule() {
        super("ClickGUI", "Menüyü açar.", Category.MENU, Keyboard.KEY_TAB);
    }

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(IEBClient.clickGUI);
        setEnabled(false);
    }
}
