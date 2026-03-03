package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class MiddleClickFriend extends Module {
    public MiddleClickFriend() {
        super("MiddleClickFriend", "Orta tıkla oyuncuları arkadaş ekler.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (Mouse.isButtonDown(2) && mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof net.minecraft.entity.player.EntityPlayer) {
            // Logic to add to friend list
        }
    }
}
