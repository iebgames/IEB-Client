package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class XRay extends Module {

    private final List<Block> blocks = new ArrayList<>();

    public XRay() {
        super("XRay", "Değerli madenleri görmeni sağlar.", Category.RENDER, Keyboard.KEY_X);
        blocks.add(Blocks.diamond_ore);
        blocks.add(Blocks.gold_ore);
        blocks.add(Blocks.iron_ore);
        blocks.add(Blocks.coal_ore);
        blocks.add(Blocks.emerald_ore);
        blocks.add(Blocks.redstone_ore);
        blocks.add(Blocks.lapis_ore);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        mc.renderGlobal.loadRenderers();
    }

    @Override
    protected void onDisable() {
        mc.renderGlobal.loadRenderers();
    }

    public boolean isXRayBlock(Block block) {
        return isEnabled() && blocks.contains(block);
    }
}
