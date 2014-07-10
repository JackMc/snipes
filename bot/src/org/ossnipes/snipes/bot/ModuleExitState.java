package org.ossnipes.snipes.bot;

/** Signals to a module under what circumstances it is being unloaded.
 * 
 * @author Jack McCracken (Auv5) */
public enum ModuleExitState
{
	/** Signals that the module was unloaded with a command or automatically by
	 * the bot (not at shutdown). */
	EXIT_UNLOAD,
	/** Signals that the module was unloaded because the bot is quitting. */
	EXIT_QUITTING,
	/** Signals that the bot does not know why the module was unloaded. */
	EXIT_UNKNOWN
}
