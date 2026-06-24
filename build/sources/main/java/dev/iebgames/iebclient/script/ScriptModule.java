package dev.iebgames.iebclient.script;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.*;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class ScriptModule extends Module {

    private final Object callback;
    private final String sourceFile;

    public ScriptModule(String name, String description, Category category, int key, Object callback, String sourceFile) {
        super(name, description, category, key);
        this.callback = callback;
        this.sourceFile = sourceFile;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    private ScriptObjectMirror mirror() {
        if (callback instanceof ScriptObjectMirror) {
            return (ScriptObjectMirror) callback;
        }
        return null;
    }

    private void invoke(String method) {
        ScriptObjectMirror mirror = mirror();
        if (mirror == null) {
            if (callback instanceof ScriptObjectMirror && "onUpdate".equals(method)) {
                ScriptObjectMirror fn = (ScriptObjectMirror) callback;
                if (fn.isFunction()) fn.call(null);
            }
            return;
        }
        if (mirror.isFunction() && "onUpdate".equals(method)) {
            mirror.call(null);
            return;
        }
        if (mirror.hasMember(method)) {
            Object member = mirror.getMember(method);
            if (member instanceof ScriptObjectMirror && ((ScriptObjectMirror) member).isFunction()) {
                ((ScriptObjectMirror) member).call(mirror);
            }
        }
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        invoke("onUpdate");
    }

    @EventHook
    public void onRender2D(EventRender2D e) {
        invoke("onRender2D");
    }

    @EventHook
    public void onRender3D(EventRender3D e) {
        invoke("onRender3D");
    }

    @EventHook
    public void onPacket(EventPacket e) {
        invoke("onPacket");
    }

    @EventHook
    public void onPreMotion(EventMotion e) {
        if (e.isPre()) invoke("onPreMotion");
    }

    @EventHook
    public void onPostMotion(EventMotion e) {
        if (!e.isPre()) invoke("onPostMotion");
    }

    @EventHook
    public void onKey(EventKey e) {
        invoke("onKey");
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        invoke("onEnable");
    }

    @Override
    protected void onDisable() {
        invoke("onDisable");
        super.onDisable();
    }
}
