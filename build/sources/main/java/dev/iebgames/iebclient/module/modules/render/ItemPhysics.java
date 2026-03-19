package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class ItemPhysics extends Module {

    public ItemPhysics() {
        super("ItemPhysics", "Düşen eşyaların fiziklerini daha gerçekçi yapar (Yere yatarlar).", Category.RENDER, Keyboard.KEY_NONE);
    }
}
