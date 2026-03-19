package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class AutoPot extends Module {

    private final NumberSetting health = register(new NumberSetting("Health", 12.0, 1.0, 20.0, 1.0));

    public AutoPot() {
        super("AutoPot", "Canın azaldığında otomatik pot atar.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.getHealth() <= health.getValue()) {
            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                if (stack != null && stack.getItem() instanceof ItemPotion) {
                    ItemPotion pot = (ItemPotion) stack.getItem();
                    if (ItemPotion.isSplash(stack.getMetadata())) {
                        List<PotionEffect> effects = pot.getEffects(stack);
                        for (PotionEffect effect : effects) {
                            if (effect.getPotionID() == Potion.heal.id || effect.getPotionID() == Potion.regeneration.id) {
                                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(i));
                                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, 90, mc.thePlayer.onGround));
                                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(stack));
                                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
