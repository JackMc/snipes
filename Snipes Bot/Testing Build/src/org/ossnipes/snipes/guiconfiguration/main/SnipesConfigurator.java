package org.ossnipes.snipes.guiconfiguration.main;

import org.ossnipes.snipes.utils.Configuration;

/**
 * Initialisation class for the Snipes GUI configuration program,
 * all this class does is load the configuration, stick it in a variable,
 * and start the main components.
 *
 * @author Jack McCracken
 */
public class SnipesConfigurator {

    /**
     * Are we in edit mode?
     */
    public static boolean EDIT_MODE = false;
    public static Configuration LOADED_CONF;
    public String[] plugins = null;

    public SnipesConfigurator() {
        plugins = Configuration.getSplitProperty("plugins");
    }
}
