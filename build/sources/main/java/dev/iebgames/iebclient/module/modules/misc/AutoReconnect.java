package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import org.lwjgl.input.Keyboard;

public class AutoReconnect extends Module {

    private int ticks;

    public AutoReconnect() {
        super("AutoReconnect", "Disconnect sonrası otomatik yeniden bağlanır.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.theWorld != null) {
            ticks = 0;
            return;
        }
        if (mc.currentScreen == null || mc.currentScreen instanceof GuiConnecting) return;
        ServerData data = mc.getCurrentServerData();
        if (data == null) return;
        if (++ticks > 100) {
            mc.displayGuiScreen(new GuiConnecting(mc.currentScreen, mc, data));
            ticks = 0;
        }
    }
}
