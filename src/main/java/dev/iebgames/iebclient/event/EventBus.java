package dev.iebgames.iebclient.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {

    private final Map<Class<?>, List<ListenerEntry>> listeners = new ConcurrentHashMap<>();

    public void subscribe(Object target) {
        for (Method method : target.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventHook.class) && method.getParameterCount() == 1) {
                Class<?> eventType = method.getParameterTypes()[0];
                method.setAccessible(true);
                listeners.computeIfAbsent(eventType, k -> new ArrayList<>())
                         .add(new ListenerEntry(target, method));
            }
        }
    }

    public void unsubscribe(Object target) {
        for (List<ListenerEntry> list : listeners.values()) {
            list.removeIf(e -> e.target == target);
        }
    }

    public <T extends Event> T publish(T event) {
        List<ListenerEntry> list = listeners.get(event.getClass());
        if (list != null) {
            for (ListenerEntry entry : list) {
                try {
                    entry.method.invoke(entry.target, event);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (event.isCancelled()) break;
            }
        }
        return event;
    }

    private static class ListenerEntry {
        final Object target;
        final Method method;
        ListenerEntry(Object target, Method method) {
            this.target = target;
            this.method = method;
        }
    }
}
