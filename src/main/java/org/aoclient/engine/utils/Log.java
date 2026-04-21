package org.aoclient.engine.utils;

import org.tinylog.Logger;

import static org.aoclient.engine.utils.LaunchOptions.CLIENT_DEBUG;

public class Log {

    // ANSI colors
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";

    public static void err(String message, Object... args) {
        Logger.error(RED + format(message, args) + RESET);
    }

    public static void warn(String message, Object... args) {
        Logger.warn(YELLOW + format(message, args) + RESET);
    }

    /**
     * log en debug, solo cliente en modo debug.
     */
    public static void debug(String message, Object... args) {
        if (CLIENT_DEBUG) {
            Logger.debug(CYAN + format(message, args) + RESET);
        }
    }

    public static void info(String message, Object... args) {
        Logger.info(WHITE + format(message, args) + RESET);
    }

    /**
     * Reemplaza {} con los argumentos
     */
    private static String format(String message, Object... args) {
        for (Object arg : args) {
            message = message.replaceFirst("\\{}", arg != null ? arg.toString() : "null");
        }
        return message;
    }
}
