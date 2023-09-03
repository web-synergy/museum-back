package baza.trainee.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoggingService {

    /**
     * It`s used to define the length of lines used in the formatted log output.
     */
    private static final int LINE_LENGTH = 40;

    /**
     * Logs an error message with a formatted output.
     *
     * @param title   The title or description of the error.
     * @param message The detailed error message.
     */
    public void logError(final String title, final String message) {
        String equalsLine = "=".repeat(LINE_LENGTH);
        String bottomLine =
                "=".repeat(title.length() + 2) + equalsLine.repeat(2);

        log.error(
                "\n\033[0;31m{} {} {}\033[0m\n\033[0;95mMessage: {}\033[0m\n\033[0;31m{}\033[0m",
                equalsLine, title, equalsLine, message, bottomLine);
    }
}
