package dev.iebgames.iebclient.module.modules.world;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public class GhostHand extends Module {

    private final NumberSetting reach = register(new NumberSetting("Reach", 5.0, 3.0, 8.0, 0.1));

    public GhostHand() {
        super("GhostHand", "Blokların arkasındaki entitylere tıklamanı sağlar.", Category.WORLD, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer == null || !mc.gameSettings.keyBindUseItem.isKeyDown()) return;
        Entity closest = null;
        double closestDist = reach.getValue();
        for (Object o : mc.theWorld.loadedEntityList) {
            if (!(o instanceof EntityPlayer) || o == mc.thePlayer) continue;
            Entity ent = (Entity) o;
            double d = mc.thePlayer.getDistanceToEntity(ent);
            if (d < closestDist) {
                closestDist = d;
                closest = ent;
            }
        }
        if (closest != null && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK) {
            mc.playerController.interactWithEntitySendPacket(mc.thePlayer, closest);
        }
    }
}
