package org.ossnipes.snipes.bot;

import java.util.HashMap;
import java.util.Map;

import org.ossnipes.snipes.lib.irc.Event;
import org.ossnipes.snipes.lib.irc.EventArgs;
import org.ossnipes.snipes.lib.irc.IRCEventListener;

public abstract class Module implements IRCEventListener
{
	public Module()
	{
		// Set all permissions to false, stopping NullPointerExceptions.
		for (ModulePermission mp : ModulePermission.values())
		{
			this._permissions.put(mp, false);
		}
	}

	/** Performs startup operations for this module */
	final void initiailise(SnipesBot parentBot)
	{
		if (parentBot == null)
		{
			throw new IllegalArgumentException("parentBot cannot be null.");
		}
		this._parentBot = parentBot;
		this.addToEvListeners();

		this.snipesInit();
	}

	private final void addToEvListeners()
	{
		this._parentBot.addEventListener(this);
	}

	// Begin abstract method definitions
	protected abstract ModuleReturn snipesInit();

	// protected abstract void snipesFini(PluginExitState status);

	// This one's here for clarity.
	@Override
	public abstract void handleEvent(Event ev, EventArgs args);

	// End abstract method definitions.

	public final SnipesBot getParent()
	{
		return this._parentBot;
	}

	public final Configuration getConfiguration()
	{
		return this._parentBot.getConfiguration();
	}

	@SuppressWarnings("static-access")
	@Override
	public final Event[] getRegisteredEvents()
	{
		return this instanceof CoreModule ? ModuleEvent.values() : Event
				.values();
	}

	final void destruct(ModuleExitState state)
	{
		// State will be used later
		this._parentBot.removeEventListener(this);
	}

	public boolean hasPermission(ModulePermission mp)
	{
		if (mp == null)
		{
			throw new IllegalArgumentException("Permission cannot be null.");
		}
		Boolean mpVal = this._permissions.get(mp);
		return mpVal == null ?
		// Impossible case, as all permissions are set to false at construction
		// (See constructor).
		// I could only see this happening if someone modifies the
		// ModulePermission enumeration to create instances of itself.
		false
				: mpVal;

	}

	void setPermission(ModulePermission permission, boolean value)
	{
		if (permission == null)
		{
			throw new IllegalArgumentException("permission cannot be null.");
		}
		this._permissions.put(permission, value);
	}

	private final Map<ModulePermission, Boolean> _permissions = new HashMap<ModulePermission, Boolean>();
	private SnipesBot _parentBot;
}
