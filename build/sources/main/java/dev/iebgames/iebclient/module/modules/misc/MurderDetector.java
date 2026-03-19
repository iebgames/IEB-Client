package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class MurderDetector extends Module {

    private final List<EntityPlayer> murderers = new ArrayList<>();

    public MurderDetector() {
        super("MurderDetector", "Murder Mystery gibi oyunlarda katili tespit eder.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer || murderers.contains(player)) continue;

            if (player.getHeldItem() != null && (player.getHeldItem().getItem() instanceof ItemSword || player.getHeldItem().getItem() instanceof ItemBow)) {
                murderers.add(player);
                IEBClient.addChatMessage("§cKATİL TESPİT EDİLDİ: §e" + player.getName());
            }
        }
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        murderers.clear();
    }
}
