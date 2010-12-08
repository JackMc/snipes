package org.ossnipes.snipes.utils;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Snipes logging class. All methods in this class are 'static.' 
 * It is used for generic logging to the default Snipes logger (defined in {@link Constants})
 * @author jack
 */
public class Log {

    private static boolean initd;
    private static Logger sLog = Logger.getLogger(Constants.LOGGERNAME);

    private static void init() {
        try {
            sLog.addHandler(new FileHandler(Constants.LOGNAME, true));
            initd = true;
        } catch (Exception e) {
        }
    }

    /** Logs to the default Snipes logger.
     * Nothing is done if the line cannot be logged.
     * @param line The line to log.
     * @param l The level to log it at.
     */
    public static void log(String line, Level l) {
        if (!initd) {
            init();
        }
        if (sLog.isLoggable(l)) {
            sLog.log(l, line);
        }
    }
}
