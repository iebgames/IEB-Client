package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

public class NameTags extends Module {

    public NameTags() {
        super("NameTags", "İsim etiketlerini büyütür ve her zaman görünür yapar.", Category.RENDER, Keyboard.KEY_NONE);
    }
}
