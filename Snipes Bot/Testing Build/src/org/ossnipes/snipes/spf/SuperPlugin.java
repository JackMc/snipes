package org.ossnipes.snipes.spf;

/**
 * <p>Base class for Super Plugins for the Snipes Plugin Framework (SPF.)
 * This class 'extends' (inherits from) the {@link PluginType} class. The purpose
 * of a Super Plugin is a {@link Plugin} With extra power over the bot.
 * Super Plugins are able to subscribe to events occuring with Snipes itself,
 * not just the IRC protocol (for example, user input to the console or
 * Snipes restarting,) and to change the behaviour of events/cancel them
 * For this reason, Super Plugins may only be loaded/unloaded by
 * 'Super Bot Administrators,' and not moderators/trusted members.</p>
 * <p/>
 * <p>Note about Snipes' internal plugin infrastructure: Snipes will call Super
 * Plugins first, so that they may cancel events before they get to normal plugins.
 * </p>
 * <p/>
 * <p>If you are creating a plugin type (a generic type of plugin that may be
 * inherited from, such as this one) you should inherit from {@link PluginType}</p>
 * <p/>
 * <p>Plugins should only inherit from SuperPlugin if they must. A good practice
 * would be to keep your plugins at the least powerful level possible.</p>
 *
 * @author Jack McCracken (Auv5)
 * @see PluginType
 * @see Plugin
 */
public abstract class SuperPlugin extends PluginType {

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean canCancelEvents() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean canHookInternalEvents() {
        return true;
    }
}
