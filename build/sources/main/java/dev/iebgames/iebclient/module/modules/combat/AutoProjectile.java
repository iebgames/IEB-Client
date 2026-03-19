package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import dev.iebgames.iebclient.util.TimerUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class AutoProjectile extends Module {

    private final NumberSetting delay = register(new NumberSetting("Delay", 500, 100, 2000, 50));
    private final TimerUtils timer = new TimerUtils();

    public AutoProjectile() {
        super("AutoProjectile", "Düşmana otomatik olarak kartopu ve yumurta fırlatır.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        KillAura aura = IEBClient.moduleManager.getModule(KillAura.class);
        if (aura == null || !aura.isEnabled()) return;

        EntityLivingBase target = aura.getTarget();
        if (target == null) return;

        if (timer.hasReached(delay.getLong())) {
            int slot = findProjectile();
            if (slot != -1) {
                int oldSlot = mc.thePlayer.inventory.currentItem;
                mc.thePlayer.inventory.currentItem = slot;
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                mc.thePlayer.inventory.currentItem = oldSlot;
                timer.reset();
            }
        }
    }

    private int findProjectile() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
            if (stack != null && (stack.getItem() instanceof ItemSnowball || stack.getItem() instanceof ItemEgg)) return i;
        }
        return -1;
    }
}
