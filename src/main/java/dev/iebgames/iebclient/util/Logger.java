package dev.iebgames.iebclient.util;

public final class Logger {
    private static final org.apache.logging.log4j.Logger LOG =
        org.apache.logging.log4j.LogManager.getLogger("IEBClient");

    public static void info (String msg) { LOG.info(msg); }
    public static void warn (String msg) { LOG.warn(msg); }
    public static void error(String msg) { LOG.error(msg); }
}
