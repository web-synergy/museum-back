package web.synergy.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * The Logger class provides a simple way to log messages
 * with different log levels (ERROR, WARNING, INFO) to the console and saves it to file.
 * It uses SLF4J for actual logging and adds formatting for colorful and styled output in the console.
 */
@Slf4j
public class Logger {

    private static final int LINE_LENGTH = 40;

    /**
     * Logs an error message with the specified title and message.
     *
     * @param title   The title of the log entry.
     * @param message The message to be logged.
     */
    public static void error(final String title, final String message) {
        logMessage("[ERROR]", title, message);
    }

    /**
     * Logs a warning message with the specified title and message.
     *
     * @param title   The title of the log entry.
     * @param message The message to be logged.
     */
    public static void warning(final String title, final String message) {
        logMessage("[WARNING]", title, message);
    }

    /**
     * Logs an info message with the specified title and message.
     *
     * @param title   The title of the log entry.
     * @param message The message to be logged.
     */
    public static void info(final String title, final String message) {
        logMessage("[INFO]", title, message);
    }

    private static String createHalfOfTopLine() {
        return "=".repeat(LINE_LENGTH);
    }

    private static String createBottomLine(String title) {
        return "=".repeat(title.length() + 2) + createHalfOfTopLine().repeat(2);
    }

    private static String getColorCode(String logLevel) {
        return switch (logLevel) {
            case "[ERROR]" -> "\033[0;31m"; // Red
            case "[WARNING]" -> "\033[0;33m"; // Yellow
            case "[INFO]" -> "\033[0;36m"; // Cyan
            default -> "\033[0;97m"; // Default white
        };
    }

    private static void logMessage(String logLevel, final String title, final String message) {
        String equalsLine = createHalfOfTopLine();
        String bottomLine = createBottomLine(title);
        String colorCode = getColorCode(logLevel);

        String formattedMessage = String.format(
                "\n%s%s %s %s%s\n\033[0;95m %s Message: %s\n%s%s\033[0m",
                colorCode, equalsLine, title, equalsLine, colorCode, logLevel, message, colorCode, bottomLine);

        switch (logLevel) {
            case "[ERROR]" -> log.error(formattedMessage);
            case "[WARNING]" -> log.warn(formattedMessage);
            case "[INFO]" -> log.info(formattedMessage);
            default -> log.debug(formattedMessage);
        }
    }
}
