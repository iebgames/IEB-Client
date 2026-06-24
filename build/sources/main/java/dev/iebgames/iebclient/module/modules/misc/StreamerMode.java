package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.BooleanSetting;
import org.lwjgl.input.Keyboard;

public class StreamerMode extends Module {

    public static boolean hideWatermark;
    public static boolean hideModules;

    private final BooleanSetting watermark = register(new BooleanSetting("Hide Watermark", true));
    private final BooleanSetting modules = register(new BooleanSetting("Hide Module List", true));

    public StreamerMode() {
        super("StreamerMode", "Stream için HUD ve isim gizleme.", Category.MISC, Keyboard.KEY_NONE);
    }

    @Override
    protected void onEnable() {
        hideWatermark = watermark.isEnabled();
        hideModules = modules.isEnabled();
    }

    @Override
    protected void onDisable() {
        hideWatermark = false;
        hideModules = false;
    }
}
