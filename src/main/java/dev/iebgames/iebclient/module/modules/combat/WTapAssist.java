package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventAttack;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class WTapAssist extends Module {

    public WTapAssist() {
        super("WTapAssist", "Vurduğunda otomatik W tuşuna bas-çek yapar.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onAttack(EventAttack e) {
        if (mc.thePlayer.isSprinting()) {
            mc.thePlayer.setSprinting(false);
            mc.thePlayer.setSprinting(true);
        }
    }
}
