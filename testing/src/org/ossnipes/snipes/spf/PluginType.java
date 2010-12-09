package org.ossnipes.snipes.spf;

import java.util.logging.Level;
import org.jibble.pircbot.PircBot;

import org.ossnipes.snipes.enums.PluginConstructRet;
import org.ossnipes.snipes.enums.PluginDestructRet;
import org.ossnipes.snipes.enums.PluginPassResponse;
import org.ossnipes.snipes.enums.SnipesEvent;
import org.ossnipes.snipes.interfaces.SnipesLogger;
import org.ossnipes.snipes.utils.Log;

/**
 * Base class used to represent a Snipes plugin type (a base 
 * class used to define the rough outline of a plugin, and what rights it should 
 * have within the bot) All <b>plugin types</b> should inherit from this type. 
 * If you create a Snipes plugin, it shouldn't inherit from this class, 
 * rather something such as Snipes Super Plugin ({@link SuperPlugin},) 
 * or Snipes Plugin ({@link Plugin}.)
 * @author Jack McCracken (Auv5)
 */
public abstract class PluginType implements SnipesLogger {

    private PircBot bot;

    /** Log a line to the default Snipes Logger.
     * This method assumes level {@link Level}.CONFIG.
     * @param line The line to log.
     */
    @Override
    public final void log(String line) {
        log(line, Level.CONFIG);
    }

    /** Log a line to the default Snipes Logger.
     * @param line The line to log.
     * @param level The level to log at.
     */
    @Override
    public final void log(String line, Level level) {
        Log.log(line, level);
    }

    /**
     * Called when a Snipes event (internal or external) that you have the rights to handle happens.
     * @param event The event, from the SnipesEvent enum
     * @param params The paramaters, works just like a ArrayList.
     * @return If you are able to cancel events (determined by your plugin type's canCancelEvents() method,) you may return PLUGIN_CANCELEVENT, if not, you may only return PLUGIN_PASSEVENT or null (does the same)
     */
    protected abstract PluginPassResponse handleEvent(SnipesEvent event, SnipesEventParams params);

    public final PluginPassResponse event(SnipesEvent event, SnipesEventParams params) {

        return handleEvent(event, params);
    }

    /** Signifies the beginning of a Plugin's lifecycle.
     * @return If the plugin initialized successfully. Null assumes yes.
     */
    public abstract PluginConstructRet snipesInit();

    /** Signifies the end of a Plugin's lifecycle.
     * @return If the plugin ended normally. Null assumes yes.
     */
    public abstract PluginDestructRet snipesFini(int status);

    /** Determines if this Plugin can hook into events beginning with
     * "SNIPES_INT".
     * @return If the Plugin can hook into internal events.
     */
    public abstract boolean canHookInternalEvents();

    /** Determines if the Plugin can cancel events for plugins below
     * it.
     * @return If the Plugin can cancel events.
     */
    public abstract boolean canCancelEvents();

    /** Gets the name of the Plugin.
     * @return The name of the Plugin.
     */
    public abstract String getName();
}
