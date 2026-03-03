package dev.iebgames.iebclient.util;

public final class TimerUtils {
    private long lastTime = 0;

    public boolean hasReached(long milliseconds) {
        return System.currentTimeMillis() - lastTime >= milliseconds;
    }

    public void reset() {
        lastTime = System.currentTimeMillis();
    }

    public long getElapsed() {
        return System.currentTimeMillis() - lastTime;
    }
}
