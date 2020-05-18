package utils;

public class Logger {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Logger.class);
    private static String suffix = "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ";
    private static String banner = "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *";
    private static String warning = "#######################";

    private Logger() {

    }

    public static void logAction(final String msg) {
        log.info("  " + msg);
        log.info("  " + suffix);
    }

    public static void logComment(final String msg) {
        log.info("        -> " + msg);
        log.info("  " + suffix);
    }

    public static void logWarning(final String msg) {
        log.info(warning);
        log.info("### WARNING: " + msg);
        log.info(warning);
    }

    public static void logStep(final String stepMsg) {
        log.info(banner);
        log.info(stepMsg);
        log.info(banner);
    }
}