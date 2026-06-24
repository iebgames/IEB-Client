package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.util.PlayerUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class ShieldBreaker extends Module {

    public ShieldBreaker() {
        super("ShieldBreaker", "Swaps to axe and attacks blocking targets (1.8 sword block).", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        EntityLivingBase target = PlayerUtils.getClosestTarget(4, true, false);
        if (target == null || !(target instanceof EntityPlayer)) return;
        if (!((EntityPlayer) target).isBlocking()) return;

        int axeSlot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack s = mc.thePlayer.inventory.getStackInSlot(i);
            if (s != null && (s.getItem() == Items.iron_axe || s.getItem() == Items.diamond_axe || s.getItem() == Items.stone_axe)) {
                axeSlot = i;
                break;
            }
        }
        if (axeSlot == -1) return;
        mc.thePlayer.inventory.currentItem = axeSlot;
        mc.playerController.attackEntity(mc.thePlayer, target);
        mc.thePlayer.swingItem();
    }
}
