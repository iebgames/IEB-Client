package dev.iebgames.iebclient.module.modules.misc;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.TextSetting;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class FriendManager extends Module {

    private final TextSetting addFriend = register(new TextSetting("Add Friend", ""));
    private final List<String> friends = new ArrayList<>();

    public FriendManager() {
        super("FriendManager", "Arkadaş listesini yönetir.", Category.MISC, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        String name = addFriend.getValue();
        if (name != null && !name.trim().isEmpty()) {
            String n = name.trim();
            if (!friends.contains(n)) friends.add(n);
            addFriend.setValue("");
            dev.iebgames.iebclient.IEBClient.addChatMessage("§aFriend added: §r" + n);
        }
    }

    public boolean isFriend(String name) {
        return friends.contains(name);
    }

    public List<String> getFriends() {
        return friends;
    }
}
