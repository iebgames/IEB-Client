package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import dev.iebgames.iebclient.util.PlayerUtils;
import dev.iebgames.iebclient.util.TimerUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class InfiniteAura extends Module {

    private final NumberSetting range = register(new NumberSetting("Range", 30.0, 10.0, 100.0, 1.0));
    private final NumberSetting targetsCount = register(new NumberSetting("Targets", 3, 1, 10, 1));
    private final NumberSetting delay = register(new NumberSetting("Delay", 500, 100, 2000, 50));
    private final TimerUtils timer = new TimerUtils();

    public InfiniteAura() {
        super("InfiniteAura", "Uzaktaki birden fazla hedefe ışınlanarak saldırır.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (!timer.hasReached(delay.getLong())) return;

        List<EntityLivingBase> targets = PlayerUtils.getTargets(range.getValue(), true, true);
        if (targets.isEmpty()) return;

        int count = 0;
        double oldX = mc.thePlayer.posX;
        double oldY = mc.thePlayer.posY;
        double oldZ = mc.thePlayer.posZ;

        for (EntityLivingBase target : targets) {
            if (count >= targetsCount.getInt()) break;

            // Simple teleport to target
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(target.posX, target.posY, target.posZ, true));
            mc.playerController.attackEntity(mc.thePlayer, target);
            mc.thePlayer.swingItem();
            
            // Teleport back
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(oldX, oldY, oldZ, true));

            count++;
        }
        timer.reset();
    }
}
