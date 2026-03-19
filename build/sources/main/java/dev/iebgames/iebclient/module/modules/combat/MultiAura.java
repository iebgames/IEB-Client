package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import dev.iebgames.iebclient.setting.BooleanSetting;
import dev.iebgames.iebclient.util.PlayerUtils;
import dev.iebgames.iebclient.util.TimerUtils;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class MultiAura extends Module {

    private final NumberSetting  range   = register(new NumberSetting("Range", 4.0, 2.0, 6.0, 0.1));
    private final NumberSetting  targets = register(new NumberSetting("Max Targets", 3, 1, 10, 1));
    private final NumberSetting  cps     = register(new NumberSetting("CPS", 10, 1, 20, 1));
    private final BooleanSetting players = register(new BooleanSetting("Players", true));
    private final BooleanSetting mobs    = register(new BooleanSetting("Mobs", true));

    private final TimerUtils timer = new TimerUtils();

    public MultiAura() {
        super("MultiAura", "Birden fazla hedefe aynı anda saldırır.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        List<EntityLivingBase> list = PlayerUtils.getTargets(range.getValue(), players.isEnabled(), mobs.isEnabled());
        if (list.isEmpty()) return;
        long delay = (long)(1000.0 / cps.getInt());
        if (!timer.hasReached(delay)) return;
        int count = 0;
        for (EntityLivingBase t : list) {
            if (count >= targets.getInt()) break;
            mc.playerController.attackEntity(mc.thePlayer, t);
            mc.thePlayer.swingItem();
            count++;
        }
        timer.reset();
    }
}
