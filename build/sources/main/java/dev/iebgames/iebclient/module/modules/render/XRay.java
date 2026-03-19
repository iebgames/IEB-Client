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
        super("XRay", "Değerli madenleri görmeni sağlar.", Category.RENDER, 0);
        blocks.add(Blocks.diamond_ore);
        blocks.add(Blocks.gold_ore);
        blocks.add(Blocks.iron_ore);
        blocks.add(Blocks.coal_ore);
        blocks.add(Blocks.emerald_ore);
        blocks.add(Blocks.redstone_ore);
        blocks.add(Blocks.lapis_ore);
        blocks.add(Blocks.diamond_block);
        blocks.add(Blocks.gold_block);
        blocks.add(Blocks.iron_block);
        blocks.add(Blocks.emerald_block);
        blocks.add(Blocks.redstone_block);
        blocks.add(Blocks.lapis_block);
        blocks.add(Blocks.mob_spawner);
        blocks.add(Blocks.mossy_cobblestone);
        blocks.add(Blocks.chest);
        blocks.add(Blocks.trapped_chest);
    }

    @Override
    protected void onEnable() {
        mc.renderGlobal.loadRenderers();
    }

    @Override
    protected void onDisable() {
        mc.renderGlobal.loadRenderers();
    }

    public boolean isXRayBlock(Block block) {
        if (!isEnabled()) return true;
        return blocks.contains(block);
    }
}
