package org.ossnipes.snipes.spf;

/**
 * Base class for all 'Normal Plugins' for the Snipes Plugin Framework (SPF.)
 * This class 'extends' (inherits from) '{@link PluginType}.'A Normal Plugin is
 * defined as a plugin that only needs to <b>know about</b> and 
 * act on <b>IRC related events</b>. Normal Plugins cannot hook into events 
 * that occur within Snipes not relating to IRC (see {@link SuperPlugin} if
 * you're interested in those events.) Normal Plugins also cannot cancel 
 * events that are sent to them for other plugins further down the list.
 * 
 * <p>Note about Snipes' internal plugin infrastructure: Snipes will call Super 
 * Plugins first, so that they may cancel events before they get to normal plugins.
 * </p>
 * 
 * <p>Plugins should only inherit from SuperPlugin if they must. A good practice
 * would be to keep your plugins at the least powerful level possible.</p>
 * 
 * <p>If you are creating a plugin type (a generic type of plugin that may be 
 * inherited from, such as this one) you should inherit from {@link PluginType}</p>
 * 
 * @author Jack McCracken (Auv5)
 */

public abstract class Plugin extends PluginType {
	@Override
	public final boolean canCancelEvents() {return false;}
	@Override
	public final boolean canHookInternalEvents() {return false;}
}
