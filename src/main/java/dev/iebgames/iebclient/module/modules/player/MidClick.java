package dev.iebgames.iebclient.module.modules.player;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;

public class MidClick extends Module {

    private boolean wasPressed = false;

    public MidClick() {
        super("MidClick", "Oyuncuya orta tıkladığında onu arkadaş listene ekler.", Category.PLAYER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.currentScreen != null) return;

        if (Mouse.isButtonDown(2)) {
            if (!wasPressed) {
                if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) mc.objectMouseOver.entityHit;
                    IEBClient.addChatMessage("Arkadaş eklendi (Simüle): " + player.getName());
                }
                wasPressed = true;
            }
        } else {
            wasPressed = false;
        }
    }
}
