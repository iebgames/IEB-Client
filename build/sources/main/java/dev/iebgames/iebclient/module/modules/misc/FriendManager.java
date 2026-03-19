package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class FriendManager extends Module {
    private final List<String> friends = new ArrayList<>();

    public FriendManager() {
        super("FriendManager", "Arkadaş listesini yönetir.", Category.MISC, Keyboard.KEY_NONE);
    }

    public boolean isFriend(String name) {
        return friends.contains(name);
    }
}
