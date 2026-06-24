package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.NumberSetting;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

import java.util.ArrayDeque;
import java.util.Deque;

public class Breadcrumbs extends Module {

    private final NumberSetting maxPoints = register(new NumberSetting("Max Points", 100, 20, 300, 10));
    private final Deque<BlockPos> trail = new ArrayDeque<>();
    private BlockPos last;

    public Breadcrumbs() {
        super("Breadcrumbs", "Geçtiğin yerde iz bırakır.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer == null) return;
        BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        if (last == null || !last.equals(pos)) {
            trail.addLast(pos);
            last = pos;
            while (trail.size() > maxPoints.getInt()) trail.removeFirst();
        }
    }

    public Deque<BlockPos> getTrail() { return trail; }
}
