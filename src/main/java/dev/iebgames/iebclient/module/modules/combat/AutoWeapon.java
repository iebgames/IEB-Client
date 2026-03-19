package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import org.lwjgl.input.Keyboard;

public class AutoWeapon extends Module {

    public AutoWeapon() {
        super("AutoWeapon", "Saldırı anında en iyi silahı otomatik seçer.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.isSwingInProgress) {
            int bestSlot = -1;
            double maxDamage = -1;

            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
                if (stack != null && (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool)) {
                    double damage = getDamage(stack);
                    if (damage > maxDamage) {
                        maxDamage = damage;
                        bestSlot = i;
                    }
                }
            }

            if (bestSlot != -1 && bestSlot != mc.thePlayer.inventory.currentItem) {
                mc.thePlayer.inventory.currentItem = bestSlot;
            }
        }
    }

    private double getDamage(ItemStack stack) {
        double damage = 0;
        if (stack.getItem() instanceof ItemSword) {
            damage += 4.0; // Default base for swords in 1.8.9
        } else if (stack.getItem() instanceof ItemTool) {
            damage += 2.0; // Default base for tools
        }
        damage += EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);
        return damage;
    }
}
