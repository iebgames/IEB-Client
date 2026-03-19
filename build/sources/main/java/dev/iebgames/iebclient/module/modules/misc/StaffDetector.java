package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public class StaffDetector extends Module {

    public StaffDetector() {
        super("StaffDetector", "Yetkilileri tespit eder ve seni uyarır.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer) continue;
            
            String name = player.getName().toLowerCase();
            if (name.contains("admin") || name.contains("mod") || name.contains("staff") || name.contains("helper")) {
                IEBClient.addChatMessage("§cDİKKAT! §fYetkili tespit edildi: §e" + player.getName());
                this.setEnabled(false);
                return;
            }
        }
    }
}
