package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventRender3D;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.ColorSetting;
import dev.iebgames.iebclient.util.EspRenderHelper;
import org.lwjgl.input.Keyboard;

public class StorageESP extends Module {

    private final ColorSetting color = register(new ColorSetting("Color", 0xB4C878FF));

    public StorageESP() {
        super("StorageESP", "Depolama bloklarını gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onRender3D(EventRender3D e) {
        EspRenderHelper.renderStorage(color.getColor(), e.getPartialTicks());
    }
}
