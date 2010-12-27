package org.ossnipes.snipes.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Snipes constants. Not really much to say.
 *
 * @author Jack McCracken (Auv5)
 */
public class Constants {

    /**
     * A comma seperated list of all the core plugins
     */
    private static String CORE_PLUGINS_STRING;
    /**
     * The name of the logger to use (Snipes internal use only)
     */
    public static final String LOGGERNAME = "SnipesDefLogger";
    /**
     * The name of the log file to use
     */
    public static final String LOGNAME = "Snipes.log";
    /**
     * Use us events (currently causes reccursion)
     */
    public static final boolean US_ENABLED = false;
    /**
     * The default PrintStream, for restoring it later
     */
    public static final PrintStream systemOutDef = System.out;
    /**
     * The path to Snipes' configuration
     */
    public static final String CONFNAME = "./snipes.properties";
    /**
     * An array of all core plugins
     */
    public static final String[] CORE_PLUGINS = {"plugins.SPFManager"};
    /**
     * If we're set to verbose, this amount of miliseconds will be counted between error queue prints.
     */
    public static final int VERBOSE_ERRT = 20000;
    /**
     * If we're set to non-verbose, this amount of miliseconds will be counted between error queues.
     */
    public static final int NON_VERBOSE_ERRT = 1000;

    public static final double VERSION = 0.5;
    public static final String VERSION_STRING = String.valueOf(VERSION);

    public static PrintStream getDevNull() throws FileNotFoundException {
        if (System.getProperty("os.name").contains("Linux") || System.getProperty("os.name").contains("Mac")) {
            return new PrintStream(new File("/dev/null"));
        } else if (System.getProperty("os.name").contains("Windows")) {
            return new PrintStream(new File("NUL"));
        }
        return null;
    }

    public static String getCorePluginsString() {
        if (CORE_PLUGINS_STRING == null) {
            for (int i = 0; i < CORE_PLUGINS.length; i++) {
                if (i != 0) {
                    CORE_PLUGINS_STRING += "," + CORE_PLUGINS[i];
                } else {
                    CORE_PLUGINS_STRING += CORE_PLUGINS[i];
                }
            }
        }
        return CORE_PLUGINS_STRING;
    }
}
