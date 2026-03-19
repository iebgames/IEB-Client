package dev.iebgames.iebclient.script;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class ScriptModule extends Module {

    private final Object callback;

    public ScriptModule(String name, String description, Category category, int key, Object callback) {
        super(name, description, category, key);
        this.callback = callback;
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (callback instanceof ScriptObjectMirror) {
            ScriptObjectMirror mirror = (ScriptObjectMirror) callback;
            if (mirror.hasMember("onUpdate")) {
                mirror.callMember("onUpdate");
            }
        }
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        if (callback instanceof ScriptObjectMirror) {
            ScriptObjectMirror mirror = (ScriptObjectMirror) callback;
            if (mirror.hasMember("onEnable")) {
                mirror.callMember("onEnable");
            }
        }
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        if (callback instanceof ScriptObjectMirror) {
            ScriptObjectMirror mirror = (ScriptObjectMirror) callback;
            if (mirror.hasMember("onDisable")) {
                mirror.callMember("onDisable");
            }
        }
    }
}
